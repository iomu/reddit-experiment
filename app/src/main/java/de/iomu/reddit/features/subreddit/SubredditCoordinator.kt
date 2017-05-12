package de.iomu.reddit.features.subreddit

import com.nytimes.android.external.store2.base.impl.Store
import de.iomu.reddit.base.mvi.BaseCoordinator
import de.iomu.reddit.base.ControllerScope
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing
import de.iomu.reddit.data.store.Subreddit
import de.iomu.reddit.util.Option
import de.iomu.reddit.util.toOption
import io.reactivex.Observable
import timber.log.Timber

import javax.inject.Inject
import javax.inject.Named

@ControllerScope
class SubredditCoordinator @Inject constructor(val renderer: SubredditRenderer,
                                               val store: Store<Listing<Link>, Subreddit>,
                                               @Named("subreddit") val subreddit: String) :
        BaseCoordinator<SubredditContract.ViewIntention, SubredditAction, SubredditResult, SubredditContract.ViewState, SubredditContract.View>() {

    override val initialState: SubredditContract.ViewState = SubredditContract.ViewState(emptyList())

    override fun attachView(view: SubredditContract.View) {
        renderer.attachView(view)
        super.attachView(view)
    }

    override fun toAction(intention: SubredditContract.ViewIntention): SubredditAction {
        return when (intention) {
            SubredditContract.ViewIntention.Refresh -> SubredditAction.LoadLinks(refresh = true)
            SubredditContract.ViewIntention.LoadMore -> SubredditAction.LoadMore
        }
    }

    override fun handleActions(actions: Observable<SubredditAction>): Observable<SubredditResult> {
        return actions.publish { share ->
            val loadMore = share.ofType(SubredditAction.LoadMore::class.java)
            val loadData = share.ofType(SubredditAction.LoadLinks::class.java)
                    .startWith(SubredditAction.LoadLinks())
                    .compose(loadLinks(loadMore))

            Observable.merge(listOf(loadData))
        }
    }

    override fun reduce(state: SubredditContract.ViewState, result: SubredditResult): SubredditContract.ViewState {
        Timber.d("Reduce: result: %s", result::class.java)
        return when (result) {
            is SubredditResult.InProgress -> state.copy(loading = true, isLoadingMore = true, links = emptyList())
            is SubredditResult.Error -> state.copy(loading = false, error = result.message)
            is SubredditResult.Successful -> state.copy(loading = false, links = result.links.children, error = "")
            SubredditResult.LoadMoreInProgress -> state.copy(isLoadingMore = true)
            SubredditResult.LoadMoreError -> state.copy(isLoadingMore = false)
            is SubredditResult.LoadMoreSuccessful -> state.copy(links = state.links.plus(result.links.children))
            SubredditResult.EndOfItems -> state.copy(isLoadingMore = false)
        }
    }

    override fun render(state: SubredditContract.ViewState) {
        renderer.render(state)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        renderer.detachView()
    }

    // TODO clean this mess up
    private fun loadLinks(loadMore: Observable<SubredditAction.LoadMore>) = { actions: Observable<SubredditAction.LoadLinks> ->
        actions.switchMap {
            val fetch = if (it.refresh) {
                { after: String? -> store.fetch(Subreddit(subreddit, after)) }
            } else {
                { after: String? -> store.get(Subreddit(subreddit, after)) }
            }

            paginate<SubredditAction.LoadMore, Option<String>, SubredditResult>(
                    loadMore,
                    Option.None,
                    { when (it) {
                        is SubredditResult.Successful -> it.links.after.toOption()
                        is SubredditResult.LoadMoreSuccessful -> it.links.after.toOption()
                        else -> null
                    } },
                    { when (it) {
                        is SubredditResult.Successful -> it.links.after == null
                        is SubredditResult.LoadMoreSuccessful -> it.links.after == null
                        else -> false
                    } },
                    { page: Int, after: Option<String> ->
                        fetch(after.orNull())//.delay(1, TimeUnit.SECONDS)
                                .map {
                                    if (page == 0) {
                                        SubredditResult.Successful(it) as SubredditResult
                                    } else {
                                        SubredditResult.LoadMoreSuccessful(it) as SubredditResult
                                    }}
                                .startWith(
                                    if (page == 0)
                                        SubredditResult.InProgress
                                    else
                                        SubredditResult.LoadMoreInProgress
                                )
                                .onErrorReturn { t ->
                                    if (page == 0)
                                        SubredditResult.Error(t.message ?: "")
                                    else
                                        SubredditResult.LoadMoreError
                                }
                    }, SubredditResult.EndOfItems)
        }
    }

    private fun <R, N, T> paginate(trigger: Observable<R>, first: N, nextPage: (T) -> N?,
                                   stop: (T) -> Boolean,
                                   fetch: (Int, N) -> Observable<T>,
                                   end: T,
                                   pageNum: Int = 0): Observable<T> {
        return fetch(pageNum, first)
                .concatMap { page ->
                    val after = nextPage(page)?.let {
                        paginate(trigger, it, nextPage, stop, fetch, end, pageNum + 1).delaySubscription(trigger)
                    } ?: Observable.empty()
                    Observable.just(page)
                            .concatWith(after)
                }.takeUntil(stop)
                .concatWith(Observable.just(end))
    }
}



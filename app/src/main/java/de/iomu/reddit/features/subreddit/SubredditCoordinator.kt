package de.iomu.reddit.features.subreddit

import com.jakewharton.rxrelay2.BehaviorRelay
import com.nytimes.android.external.store2.base.impl.Store
import de.iomu.reddit.base.mvi.BaseCoordinator
import de.iomu.reddit.base.ControllerScope
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing
import de.iomu.reddit.data.store.Subreddit
import de.iomu.reddit.util.Option
import de.iomu.reddit.util.toOption
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.concurrent.TimeUnit

import javax.inject.Inject

@ControllerScope
class SubredditCoordinator @Inject constructor(val renderer: SubredditRenderer, val store: Store<Listing<Link>, Subreddit>) :
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
            is SubredditResult.InProgress -> state.copy(loading = true, links = emptyList())
            is SubredditResult.Error -> state.copy(loading = false, error = result.message)
            is SubredditResult.Successful -> state.copy(loading = false, links = result.links.children, error = "")
            SubredditResult.LoadMoreInProgress -> state.copy(isLoadingMore = true)
            SubredditResult.LoadMoreError -> state.copy(isLoadingMore = false)
            is SubredditResult.LoadMoreSuccessful -> state.copy(links = state.links.plus(result.links.children))
        }
    }

    override fun render(state: SubredditContract.ViewState) {
        renderer.render(state)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        renderer.detachView()
    }
    
    private fun <R, N, T> paginate(requests: Observable<R>, first: N, nextPage: (T) -> N?,
                                   stop: (T) -> Boolean, fetch: (R, N) -> Observable<T>): Observable<T> {
        val prev = BehaviorRelay.create<N>()
        prev.accept(first)
        return requests.zipWith(prev, BiFunction<R, N, Pair<R, N>> { t1, t2 -> t1 to t2 })
                .concatMap { (r, p) ->
                    fetch(r, p).map { nextPage(it)?.let { n -> prev.accept(n) }; it }
                }.takeUntil(stop)
    }

    // TODO clean this mess up
    private fun loadLinks(loadMore: Observable<SubredditAction.LoadMore>) = { actions: Observable<SubredditAction.LoadLinks> ->
        actions.flatMap {
            val fetch = if (it.refresh) {
                { after: String? -> store.fetch(Subreddit("android", after)) }
            } else {
                { after: String? -> store.get(Subreddit("android", after)) }
            }

            paginate2<SubredditAction.LoadMore, Option<String>, SubredditResult>(
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
                        fetch(after.orNull()).delay(1, TimeUnit.SECONDS)
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
                    })
        }
    }

    private fun <R, N, T> paginate2(trigger: Observable<R>, first: N, nextPage: (T) -> N?, stop: (T) -> Boolean, fetch: (Int, N) -> Observable<T>, pageNum: Int = 0): Observable<T> {
        return fetch(pageNum, first)
                .concatMap { page ->
                    val after = nextPage(page)?.let {
                        trigger.take(1).ignoreElements().andThen(paginate2(trigger, it, nextPage, stop, fetch, pageNum + 1))
                    } ?: Observable.empty()
                    Observable.just(page)
                            .concatWith(after)
                }.takeUntil(stop)
    }
}



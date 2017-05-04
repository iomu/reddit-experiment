package de.iomu.reddit.features.subreddit

import com.jakewharton.rxrelay2.PublishRelay
import com.nytimes.android.external.store2.base.impl.Store
import de.iomu.reddit.base.ControllerScope
import de.iomu.reddit.base.Coordinator
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing
import de.iomu.reddit.data.store.Subreddit
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@ControllerScope
class SubredditCoordinator @Inject constructor(val renderer: SubredditRenderer, val store: Store<Listing<Link>, Subreddit>) : Coordinator<SubredditContract.View> {
    private val disposable = CompositeDisposable()
    private val loadLinks = { actions: Observable<SubredditContract.Action.LoadLinks> ->
        actions.flatMap {
            val data = if (it.refresh) {
                store.fetch(Subreddit("android"))
            } else {
                store.get(Subreddit("android"))
            }
            data.map { SubredditContract.Result.Successful(it) as SubredditContract.Result }
                .startWith(SubredditContract.Result.InProgress)
                .onErrorReturn { t -> SubredditContract.Result.Error(t.message ?: "") }
        }
    }

    private val intentionRelay = PublishRelay.create<SubredditContract.ViewIntention>()

    init {
        val actions = intentionRelay.map(this::toAction)
        val results = handleActions(actions)
        val init = SubredditContract.ViewState(emptyList())
        val states = results.scan(init, this::reduce).skip(1)
        states.subscribe { renderer.render(it) }
    }

    override fun attachView(view: SubredditContract.View) {
        renderer.attachView(view)
        disposable.add(view.intentions.subscribe(intentionRelay))
    }

    private fun toAction(intention: SubredditContract.ViewIntention): SubredditContract.Action {
        return when (intention) {
            is SubredditContract.ViewIntention.Refresh -> SubredditContract.Action.LoadLinks(refresh = true)
        }
    }

    private fun handleActions(actions: Observable<SubredditContract.Action>): Observable<SubredditContract.Result> {
        return actions.publish { share ->
            val loadData = share.ofType(SubredditContract.Action.LoadLinks::class.java)
                    .startWith(SubredditContract.Action.LoadLinks())
                    .compose(loadLinks)

            Observable.merge(listOf(loadData))
        }
    }

    private fun reduce(state: SubredditContract.ViewState, result: SubredditContract.Result): SubredditContract.ViewState {
        Timber.d("Reduce: state: %s, result: %s", state, result)
        return when (result) {
            is SubredditContract.Result.InProgress -> state.copy(loading = true)
            is SubredditContract.Result.Error -> state.copy(loading = false, error = result.message)
            is SubredditContract.Result.Successful -> state.copy(loading = false, links = result.links.children, error = "")
        }
    }

    override fun detachView(retainInstance: Boolean) {
        renderer.detachView()
        disposable.clear()
    }
}
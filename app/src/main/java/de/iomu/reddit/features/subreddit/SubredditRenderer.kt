package de.iomu.reddit.features.subreddit

import com.jakewharton.rxrelay2.BehaviorRelay
import de.iomu.reddit.base.StateRenderer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class SubredditRenderer @Inject constructor(): StateRenderer<SubredditContract.ViewState> {
    private val stateStream = BehaviorRelay.create<SubredditContract.ViewState>()
    private val disposables = CompositeDisposable()

    override fun render(state: SubredditContract.ViewState) {
        Timber.d("Received state: %s", state)
        stateStream.accept(state)
    }

    fun attachView(view: SubredditContract.View) {
        val viewActions = viewActions(stateStream)
        disposables.add(viewActions.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.apply(it)
                })
    }

    fun detachView() {
        disposables.clear()
    }

    fun viewActions(stateStream: Observable<SubredditContract.ViewState>): Observable<SubredditContract.ViewAction> {
        return stateStream.publish { shared ->
            val errors = shared
                    .map { it.error }
                    .filter { it != "" }
                    .distinctUntilChanged()
                    .map { SubredditContract.ViewAction.ShowError(it) }
            val links = shared
                    .map { it.links }
                    .distinctUntilChanged()
                    .map { SubredditContract.ViewAction.SetLinks(it) }
            val loading = shared.map { it.loading }
                    .distinctUntilChanged()
                    .doOnNext { Timber.d("Loading $it") }
                    .map { loading ->
                        if (loading) {
                            SubredditContract.ViewAction.ShowLoading
                        } else {
                            SubredditContract.ViewAction.HideLoading
                        }
                    }
            Observable.merge(listOf(errors, links, loading)).doOnNext { Timber.d("Created action: $it") }
        }

    }
}
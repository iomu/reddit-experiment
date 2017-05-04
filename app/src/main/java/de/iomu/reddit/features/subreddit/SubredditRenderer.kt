package de.iomu.reddit.features.subreddit

import com.jakewharton.rxrelay2.BehaviorRelay
import de.iomu.reddit.base.mvi.RxRenderer
import de.iomu.reddit.base.mvi.StateRenderer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class SubredditRenderer @Inject constructor(): RxRenderer<SubredditContract.ViewState, SubredditContract.ViewAction, SubredditContract.View>() {
    override fun viewActions(stateStream: Observable<SubredditContract.ViewState>): Observable<SubredditContract.ViewAction> {
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
                    .map { loading ->
                        if (loading) {
                            SubredditContract.ViewAction.ShowLoading
                        } else {
                            SubredditContract.ViewAction.HideLoading
                        }
                    }
            Observable.merge(listOf(errors, links, loading))
        }
    }
}
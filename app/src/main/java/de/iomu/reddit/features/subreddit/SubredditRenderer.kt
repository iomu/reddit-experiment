package de.iomu.reddit.features.subreddit

import com.jakewharton.rxrelay2.BehaviorRelay
import de.iomu.reddit.base.mvi.RxRenderer
import de.iomu.reddit.base.mvi.StateRenderer
import de.iomu.reddit.data.model.Link
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
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

            val linkChange = links.startWith(emptyList<Link>())
                    .zipWith(links, BiFunction { old: List<Link>, new: List<Link> ->
                        if (old.all { new.contains(it) } && old.isNotEmpty()) {
                            SubredditContract.ViewAction.AddLinks(new.minus(old))
                        } else {
                            SubredditContract.ViewAction.SetLinks(new)
                        }
                    })

            val loading = shared.map { it.loading }
                    .distinctUntilChanged()
                    .map { loading ->
                        if (loading) {
                            SubredditContract.ViewAction.ShowLoading
                        } else {
                            SubredditContract.ViewAction.HideLoading
                        }
                    }

            val loadingMore = shared.map { it.isLoadingMore }
                    .distinctUntilChanged()
                    .map { loading ->
                        if (loading) {
                            SubredditContract.ViewAction.ShowLoadingMore
                        } else {
                            SubredditContract.ViewAction.HideLoadingMore
                        }
                    }
            Observable.merge(listOf(errors, loading, linkChange, loadingMore))
        }
    }
}
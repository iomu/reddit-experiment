package de.iomu.reddit_experiment.features.subreddit

import de.iomu.reddit_experiment.base.mvi.RxRenderer
import de.iomu.reddit_experiment.data.model.Link
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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
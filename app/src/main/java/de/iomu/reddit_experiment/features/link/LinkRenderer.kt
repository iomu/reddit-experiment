package de.iomu.reddit_experiment.features.link

import de.iomu.reddit_experiment.base.ControllerScope
import de.iomu.reddit_experiment.base.mvi.RxRenderer
import de.iomu.reddit_experiment.util.minimumDelay
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ControllerScope
class LinkRenderer @Inject constructor() : RxRenderer<LinkContract.ViewState, LinkContract.ViewAction, LinkContract.View>() {
    override fun viewActions(stateStream: Observable<LinkContract.ViewState>): Observable<LinkContract.ViewAction> {
        return stateStream.publish { shared ->

            val link = shared
                    .filter { it.link != null }
                    .map { it.link }
                    .distinctUntilChanged()
                    .map { LinkContract.ViewAction.ShowLink(it!!) }

            val comments = shared.map { it.comments }
                    .distinctUntilChanged()
                    .map { LinkContract.ViewAction.ShowComments(it) }
                 //   .minimumDelay(750, TimeUnit.MILLISECONDS)

            val errors = shared.map { it.errorMessage }
                    .filter { it != "" }
                    .distinctUntilChanged()
                    .map { LinkContract.ViewAction.ShowError(it) }

            val loading = shared.map { it.loading }
                    .distinctUntilChanged()
                    .map {
                        if (it) {
                            LinkContract.ViewAction.ShowLoading
                        } else {
                            LinkContract.ViewAction.HideLoading
                        }
                    }
                    //.minimumDelay(750, TimeUnit.MILLISECONDS)

            Observable.merge(listOf(link, comments, errors, loading))
        }
    }
}
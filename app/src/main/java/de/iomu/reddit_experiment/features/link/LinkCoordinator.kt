package de.iomu.reddit_experiment.features.link

import com.nytimes.android.external.store2.base.impl.Store
import de.iomu.reddit_experiment.base.ControllerScope
import de.iomu.reddit_experiment.base.mvi.BaseCoordinator
import de.iomu.reddit_experiment.data.model.CommentResponse
import de.iomu.reddit_experiment.data.store.LinkKey
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

@ControllerScope
class LinkCoordinator @Inject constructor(@Named("link_id") private val linkId: String,
                                          private val renderer: LinkRenderer,
                                          private val store: Store<CommentResponse, LinkKey>) :
        BaseCoordinator<LinkContract.ViewIntention, LinkAction,
                LinkResult, LinkContract.ViewState, LinkContract.View>() {

    override val initialState: LinkContract.ViewState = LinkContract.ViewState(null, emptyList(), false)

    private val loadComments: (Observable<LinkAction.LoadComments>) -> Observable<LinkResult> = { actions ->
        actions.switchMap {
            val stream = if (it.refresh) {
                store.fetch(LinkKey(linkId, 1))
            } else {
                store.get(LinkKey(linkId, 1))
            }
            stream.map { LinkResult.LoadCommentsSuccess(it.comments.children.map { it }) as LinkResult }
                    .startWith(LinkResult.LoadCommentsInProgress)
                    .onErrorReturn { LinkResult.Failure(it.message ?: "") }

        }
    }

    private val loadLink: (Observable<LinkAction.LoadLink>) -> Observable<LinkResult> = { actions ->
        actions.switchMap {
            val stream = if (it.refresh) {
                store.fetch(LinkKey(linkId, 1))
            } else {
                store.get(LinkKey(linkId, 1))
            }
            stream.map { LinkResult.LoadLinkSuccess(it.link) as LinkResult }
                    .startWith(LinkResult.LoadLinkInProgress)
                    .onErrorReturn { LinkResult.LoadLinkFailure(it.message ?: "") }

        }
    }
    override fun attachView(view: LinkContract.View) {
        super.attachView(view)
        renderer.attachView(view)
    }

    override fun detachView(retainInstance: Boolean) {
        renderer.detachView()
        super.detachView(retainInstance)
    }

    override fun toAction(intention: LinkContract.ViewIntention): LinkAction = when (intention) {
        is LinkContract.ViewIntention.Refresh -> LinkAction.LoadComments(true)
    }

    override fun handleActions(actions: Observable<LinkAction>): Observable<LinkResult> = actions.publish { shared ->
        val comments = shared.ofType(LinkAction.LoadComments::class.java)
                .startWith(LinkAction.LoadComments(false))
                .compose(loadComments)
        val link = shared.ofType(LinkAction.LoadLink::class.java)
                .startWith(LinkAction.LoadLink(false))
                .compose(loadLink)
        Observable.merge(listOf(link, comments))
    }

    override fun reduce(state: LinkContract.ViewState, result: LinkResult): LinkContract.ViewState = when (result) {
        LinkResult.LoadCommentsInProgress -> state.copy(comments = emptyList(), loading = true)
        is LinkResult.LoadCommentsSuccess -> state.copy(comments = result.comments, loading = false)
        is LinkResult.Failure -> state.copy(loading = false, errorMessage = result.message)
        is LinkResult.LoadLinkSuccess -> state.copy(link = result.link)
        else -> state
    }

    override fun render(state: LinkContract.ViewState) {
        renderer.render(state)
    }
}
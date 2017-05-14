package de.iomu.reddit_experiment.features.link

import de.iomu.reddit_experiment.base.mvi.MviView
import de.iomu.reddit_experiment.data.model.CommentItem
import de.iomu.reddit_experiment.data.model.CommentModel
import de.iomu.reddit_experiment.data.model.Link

interface LinkContract {
    sealed class ViewIntention {
        object Refresh : ViewIntention()
    }

    sealed class ViewAction {
        data class ShowLink(val link: Link) : ViewAction()
        data class ShowComments(val comments: List<CommentItem>) : ViewAction()
        data class ShowError(val message: String) : ViewAction()
        object ShowLoading : ViewAction()
        object HideLoading : ViewAction()
    }

    interface View : MviView<ViewIntention, ViewAction>

    data class ViewState(val link: Link?, val comments: List<CommentItem>, val loading: Boolean = true,
                         val errorMessage: String = "")
}

sealed class LinkAction {
    data class LoadComments(val refresh: Boolean = false) : LinkAction()
    data class LoadLink(val refresh: Boolean = false) : LinkAction()
}

sealed class LinkResult {
    object LoadCommentsInProgress : LinkResult()
    data class LoadCommentsSuccess(val comments: List<CommentItem>) : LinkResult()
    data class Failure(val message: String) : LinkResult()

    data class LoadLinkSuccess(val link: Link) : LinkResult()
    object LoadLinkInProgress : LinkResult()
    data class LoadLinkFailure(val message: String) : LinkResult()
}
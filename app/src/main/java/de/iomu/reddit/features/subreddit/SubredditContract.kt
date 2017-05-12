package de.iomu.reddit.features.subreddit

import de.iomu.reddit.base.mvi.MviView
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing

interface SubredditContract {
    sealed class ViewIntention {
        object Refresh : ViewIntention()
        object LoadMore : ViewIntention()
    }

    sealed class ViewAction {
        data class ShowError(val message: String) : ViewAction()
        data class SetLinks(val links: List<Link>) : ViewAction()
        data class AddLinks(val links: List<Link>) : ViewAction()
        object ShowLoading : ViewAction()
        object HideLoading : ViewAction()
        object ShowLoadingMore : ViewAction()
        object HideLoadingMore : ViewAction()
    }

    interface View : MviView<ViewIntention, ViewAction>



    data class ViewState(val links: List<Link>, val loading: Boolean = false, val error: String = "",
                         val isLoadingMore: Boolean = false)
}

sealed class SubredditResult {
    data class Error(val message: String) : SubredditResult()
    data class Successful(val links: Listing<Link>) : SubredditResult()
    object InProgress : SubredditResult()
    object LoadMoreInProgress : SubredditResult()
    object LoadMoreError : SubredditResult()
    object EndOfItems : SubredditResult()
    data class LoadMoreSuccessful(val links: Listing<Link>) : SubredditResult()
}

sealed class SubredditAction {
    data class LoadLinks(val refresh: Boolean = false, val after: String? = null) : SubredditAction()
    object LoadMore : SubredditAction()
}
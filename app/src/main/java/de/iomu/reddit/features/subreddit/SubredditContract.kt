package de.iomu.reddit.features.subreddit

import de.iomu.reddit.base.mvi.MviView
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing

interface SubredditContract {
    sealed class ViewIntention {
        object Refresh : ViewIntention()
    }

    sealed class ViewAction {
        data class ShowError(val message: String) : ViewAction()
        data class SetLinks(val links: List<Link>) : ViewAction()
        data class AddLinks(val links: List<Link>) : ViewAction()
        object ShowLoading : ViewAction()
        object HideLoading : ViewAction()
    }

    interface View : MviView<ViewIntention, ViewAction>

    sealed class Action {
        data class LoadLinks(val refresh: Boolean = false, val after: String? = null) : Action()
    }

    sealed class Result {
        data class Error(val message: String) : Result()
        data class Successful(val links: Listing<Link>) : Result()
        object InProgress : Result()
    }

    data class ViewState(val links: List<Link>, val loading: Boolean = false, val error: String = "")
}
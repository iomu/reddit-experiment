package de.iomu.reddit.features.subreddit

import android.os.Bundle
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.litho.*
import com.facebook.litho.widget.*
import com.facebook.yoga.YogaAlign
import com.jakewharton.rxrelay2.PublishRelay
import de.iomu.reddit.R
import de.iomu.reddit.base.BaseController
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.ui.components.EmptyComponent
import de.iomu.reddit.ui.components.LoadingListItem
import de.iomu.reddit.ui.components.RecyclerWrapper
import de.iomu.reddit.ui.components.TextLinkItem
import de.iomu.reddit.util.EndlessRecyclerScrollListener
import io.reactivex.Observable

import timber.log.Timber
import javax.inject.Inject

class SubredditController(args: Bundle) : BaseController(args), SubredditContract.View {
    private val pullToRefreshRelay = PublishRelay.create<SubredditContract.ViewIntention.Refresh>()
    private val loadMoreRelay = PublishRelay.create<SubredditContract.ViewIntention.LoadMore>()

    override val intentions: Observable<SubredditContract.ViewIntention>
        get()  {
            return Observable.merge(listOf(pullToRefreshRelay, loadMoreRelay))
        }

    var subreddit: String = args.getString(KEY_SUBREDDIT, "androiddev")

    @Inject
    lateinit var coordinator: SubredditCoordinator

    lateinit var binder: RecyclerBinder
    lateinit var context: ComponentContext
    private val recyclerController = RecyclerEventsController()
    lateinit var scrollListener: EndlessRecyclerScrollListener

    constructor(subreddit: String) : this(createBundle(subreddit)) {
        this.subreddit = subreddit
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup): View {
        context = ComponentContext(container.context)
        binder = RecyclerBinder(context, LinearLayoutInfo(container.context, OrientationHelper.VERTICAL, false))
        scrollListener = EndlessRecyclerScrollListener(binder) {
            Timber.d("Load more")
            loadMoreRelay.accept(SubredditContract.ViewIntention.LoadMore)
        }
        return LithoView.create(
                container.context,
                RecyclerWrapper.create(context)
                        .binder(binder)
                        .controller(recyclerController)
                        .refreshListener {
                            pullToRefreshRelay.accept(SubredditContract.ViewIntention.Refresh)
                        }
                        .recyclerViewId(R.id.link_list)
                        .onScrollListener(scrollListener)
                        .build())
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
      //  binder.insertItemAt(0, LoadingListItem.create(context).loading(false).build())
        coordinator.attachView(this)
    }

    override fun onDestroyView(view: View) {
        coordinator.detachView(activity?.isChangingConfigurations ?: false)
        super.onDestroyView(view)
    }

    override fun apply(action: SubredditContract.ViewAction) {
        Timber.d("SubredditAction: %s", action::class.java)
        when (action) {
            is SubredditContract.ViewAction.SetLinks -> {
                scrollListener.reset()
                displayLinks(action.links)
            }
            is SubredditContract.ViewAction.HideLoading -> recyclerController.clearRefreshing()
            is SubredditContract.ViewAction.AddLinks -> addLinks(action.links)
            is SubredditContract.ViewAction.ShowLoadingMore -> updateLoadingMore(true)
            is SubredditContract.ViewAction.HideLoadingMore -> updateLoadingMore(false)
        }
    }

    private fun updateLoadingMore(loading: Boolean) {
       if (!loading) {
           binder.updateItemAt(binder.itemCount - 1, LoadingListItem.create(context).loading(loading).build())
       }

    }

    private fun addLinks(links: List<Link>) {
        val infos = links.map {
            TextLinkItem.create(context)
                    .link(it)
                    .listener { Timber.d(it.toString()) }
                    .build()
        }.map {
            ComponentInfo.create()
                    .component(it)
                    .build()
        }
        binder.insertRangeAt(binder.itemCount - 1, infos)
        Timber.d("Last link: %s", links.lastOrNull()?.id())
    }

    private fun displayLinks(links: List<Link>) {
        val infos = links.map {
            TextLinkItem.create(context)
                    .link(it)
                    .listener { Timber.d(it.toString()) }
                    .build()
        }
            .plus(LoadingListItem.create(context).loading(true).build())
            .map {
                ComponentInfo.create()
                    .component(it)
                    .build()
            }
        binder.removeRangeAt(0, binder.itemCount)
        binder.insertRangeAt(0, infos)
    }

    companion object {
        val KEY_SUBREDDIT = "key_subreddit"

        fun createBundle(subreddit: String): Bundle {
            return Bundle().apply {
                putString(KEY_SUBREDDIT, subreddit)
            }
        }
    }
}
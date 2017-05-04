package de.iomu.reddit.features.subreddit

import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentInfo
import com.facebook.litho.LithoView
import com.facebook.litho.widget.LinearLayoutInfo
import com.facebook.litho.widget.Recycler
import com.facebook.litho.widget.RecyclerBinder
import de.iomu.reddit.R
import de.iomu.reddit.base.BaseController
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.ui.components.TextLinkItem
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class SubredditController : BaseController(), SubredditContract.View {
    override val intentions: Observable<SubredditContract.ViewIntention>
        get()  {
            return Observable.empty<SubredditContract.ViewIntention>()
        }

    @Inject
    lateinit var coordinator: SubredditCoordinator

    lateinit var binder: RecyclerBinder
    lateinit var context: ComponentContext

    override fun inflate(inflater: LayoutInflater, container: ViewGroup): View {
        context = ComponentContext(container.context)
        binder = RecyclerBinder(context, LinearLayoutInfo(container.context, OrientationHelper.VERTICAL, false))
        return LithoView.create(
                container.context,
                Recycler.create(context)
                        .binder(binder)
                        .recyclerViewId(R.id.link_list)
                        .build())
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        coordinator.attachView(this)
    }

    override fun onDestroyView(view: View) {
        coordinator.detachView(activity?.isChangingConfigurations ?: false)
        super.onDestroyView(view)
    }

    override fun apply(action: SubredditContract.ViewAction) {
        when (action) {
            is SubredditContract.ViewAction.SetLinks -> displayLinks(action.links)
        }
    }

    private fun displayLinks(links: List<Link>) {
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
        binder.insertRangeAt(0, infos)
    }
}
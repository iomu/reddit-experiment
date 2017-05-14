package de.iomu.reddit_experiment.features.link

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import de.iomu.reddit_experiment.R
import de.iomu.reddit_experiment.base.BaseController
import de.iomu.reddit_experiment.data.model.CommentItem
import de.iomu.reddit_experiment.data.model.Link
import de.iomu.reddit_experiment.kotterknife.Resetter
import de.iomu.reddit_experiment.kotterknife.bindView
import de.iomu.reddit_experiment.ui.views.LinkView
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class LinkController(args: Bundle) : BaseController(args), LinkContract.View {
    var linkId: String = args.getString(KEY_LINK)
    private var link: Link? = null

    @Inject
    lateinit var coordinator: LinkCoordinator

    private val unbinder = Resetter()

    private val linkView by bindView<LinkView>(R.id.link_item, unbinder)
    private val commentList by bindView<RecyclerView>(R.id.comment_list, unbinder)
    private val refreshLayout by bindView<SwipeRefreshLayout>(R.id.refresh_layout, unbinder)

    private val adapter = TopLevelCommentAdapter()

    private val refreshRelay = PublishRelay.create<Unit>()

    constructor(linkId: String) : this(createBundle(linkId))

    constructor(link: Link) : this(link.id()) {
        this.link = link
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_link, container, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        coordinator.attachView(this)
        link?.let { displayLink(it) }

        setupList()
    }

    override fun onDestroyView(view: View) {
        unbinder.reset()
        coordinator.detachView(activity?.isChangingConfigurations ?: false)
        super.onDestroyView(view)
    }

    override val intentions: Observable<LinkContract.ViewIntention>
        get() = Observable.merge(listOf(refreshRelay.map { LinkContract.ViewIntention.Refresh }))

    override fun apply(action: LinkContract.ViewAction) {
        Timber.d("ViewAction: ${action.javaClass}")
        val ignored = when (action) {
            is LinkContract.ViewAction.ShowLink -> displayLink(action.link)
            is LinkContract.ViewAction.ShowComments -> displayComments(action.comments)
            is LinkContract.ViewAction.ShowError -> Unit
            LinkContract.ViewAction.ShowLoading -> refreshLayout.isRefreshing = true
            LinkContract.ViewAction.HideLoading -> refreshLayout.isRefreshing = false
        }
    }

    private fun setupList() {
        commentList.adapter = adapter
        commentList.layoutManager = LinearLayoutManager(commentList.context)
        commentList.setHasFixedSize(true)
        commentList.addItemDecoration(DividerItemDecoration(commentList.context, LinearLayoutManager.VERTICAL))

        refreshLayout.setOnRefreshListener {
            refreshRelay.accept(Unit)
        }
    }

    private fun displayLink(link: Link) {
        linkView.title = link.title()
        linkView.author = link.author()
    }

    private fun displayComments(comments: List<CommentItem>) {
        adapter.setComments(comments)
    }

    companion object {
        val KEY_LINK = "link_id"

        fun createBundle(link: String): Bundle {
            return Bundle().apply {
                putString(KEY_LINK, link)
            }
        }
    }
}
package de.iomu.reddit_experiment.ui.components

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView

import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.OnEvent
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.*

@LayoutSpec
class RecyclerWrapperSpec {
    companion object {
        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(context: ComponentContext, @Prop controller: RecyclerEventsController, @Prop binder: RecyclerBinder,
                           @Prop recyclerViewId: Int, @Prop(optional = true) onScrollListener: RecyclerView.OnScrollListener): ComponentLayout {
            val builder =  Recycler.create(context)
                    .binder(binder)
                    .recyclerEventsController(controller)
                    .recyclerViewId(recyclerViewId)
                    .itemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    .onScrollListener(onScrollListener)
                    .refreshHandler(RecyclerWrapper.onPullToRefresh(context))
            return builder.buildWithLayout()
        }

        @JvmStatic
        @OnEvent(PTRRefreshEvent::class)
        fun onPullToRefresh(context: ComponentContext, @Prop(optional = true) refreshListener: () -> Unit) {
            refreshListener()
        }
    }
}
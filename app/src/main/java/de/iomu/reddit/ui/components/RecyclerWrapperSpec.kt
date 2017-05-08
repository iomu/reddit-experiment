package de.iomu.reddit.ui.components

import android.support.v7.widget.RecyclerView
import com.facebook.litho.ClickEvent
import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.OnEvent
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.*
import com.facebook.yoga.YogaEdge
import de.iomu.reddit.data.model.Link

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
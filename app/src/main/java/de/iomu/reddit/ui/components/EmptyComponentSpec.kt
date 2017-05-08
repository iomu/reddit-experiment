package de.iomu.reddit.ui.components

import com.facebook.litho.ClickEvent
import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.OnEvent
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge
import de.iomu.reddit.data.model.Link

@LayoutSpec
class EmptyComponentSpec {
    companion object {
        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(context: ComponentContext): ComponentLayout {
            return Column.create(context)
                   .build()
        }

    }
}
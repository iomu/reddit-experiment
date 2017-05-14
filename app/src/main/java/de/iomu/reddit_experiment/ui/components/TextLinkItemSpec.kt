package de.iomu.reddit_experiment.ui.components

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
import de.iomu.reddit_experiment.data.model.Link

@LayoutSpec
class TextLinkItemSpec {
    companion object {
        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(context: ComponentContext, @Prop link: Link): ComponentLayout {
            return Column.create(context)
                    .paddingDip(YogaEdge.HORIZONTAL, 16)
                    .paddingDip(YogaEdge.VERTICAL, 8)
                    .backgroundAttr(android.R.attr.selectableItemBackground)
                   // .backgroundColor(Color.WHITE)
                    .child(
                            Text.create(context)
                                    .text(link.title())
                                    .textSizeSp(20f)
                    )
                    .child(
                            Text.create(context)
                                    .text(link.author())
                                    .textSizeSp(14f)
                    )
                    .clickHandler(TextLinkItem.onClick(context))
                    .build()
        }

        @JvmStatic
        @OnEvent(ClickEvent::class)
        fun onClick(context: ComponentContext, @Prop link: Link, @Prop(optional = true) listener: (Link) -> Unit) {
            listener(link)
        }
    }
}
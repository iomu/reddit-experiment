package de.iomu.reddit_experiment.ui.components

import android.graphics.Color
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.Row
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Progress
import com.facebook.yoga.YogaJustify

@LayoutSpec
class LoadingListItemSpec {
    companion object {
        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(context: ComponentContext, @Prop loading: Boolean): ComponentLayout {
            val child = if (loading) {
                Progress.create(context)
                        .color(Color.BLUE)
                        .build()
            } else {
                EmptyComponent.create(context).build()
            }
            return Row.create(context)
                    .child(child)
                    .heightDip(32)
                    .justifyContent(YogaJustify.CENTER)
                    .build()
        }

    }
}
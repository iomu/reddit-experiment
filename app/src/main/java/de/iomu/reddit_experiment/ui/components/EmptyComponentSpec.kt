package de.iomu.reddit_experiment.ui.components

import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout

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
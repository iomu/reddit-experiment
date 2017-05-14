package de.iomu.reddit_experiment.ui.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import butterknife.bindView
import de.iomu.reddit_experiment.R
import kotlin.properties.Delegates

class LinkView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    private val titleText: TextView by bindView(R.id.link_title)

    private val authorText: TextView by bindView(R.id.link_author)

    var title: String
        get() = titleText.text.toString()
        set(value) {
            titleText.text = value
        }

    var author: String
        get() = authorText.text.toString()
        set(value) {
            authorText.text = value
        }
}
package de.iomu.reddit_experiment.ui.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import butterknife.bindView
import de.iomu.reddit_experiment.R
import kotlin.properties.Delegates

class CommentView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    private val commentText: TextView by bindView(R.id.comment_text)
    private val authorText: TextView by bindView(R.id.comment_author)

    var text: String
        get() = commentText.text.toString()
        set(value) {
            commentText.text = value
        }

    var author: String
        get() = authorText.text.toString()
        set(value) {
            authorText.text = value
        }
}
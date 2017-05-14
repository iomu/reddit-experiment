package de.iomu.reddit_experiment.features.link

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.iomu.reddit_experiment.R
import de.iomu.reddit_experiment.data.model.CommentItem
import de.iomu.reddit_experiment.data.model.CommentModel
import de.iomu.reddit_experiment.ui.views.CommentView

class TopLevelCommentAdapter : RecyclerView.Adapter<TopLevelCommentAdapter.CommentViewHolder>() {
    private val comments = mutableListOf<CommentModel>()

    fun setComments(comments: List<CommentItem>) {
        notifyItemRangeRemoved(0, itemCount)
        this.comments.clear()

        this.comments.addAll(comments.filter { it is CommentItem.Comment }.map { (it as CommentItem.Comment).comment })
        notifyItemRangeInserted(0, itemCount)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent, false) as CommentView
        return CommentViewHolder(view)
    }

    class CommentViewHolder(val commentView: CommentView) : RecyclerView.ViewHolder(commentView) {
        fun bind(commentModel: CommentModel) {
            commentView.author = commentModel.author()
            commentView.text = commentModel.body().trim()
            commentView.setOnClickListener {  }
        }
    }
}
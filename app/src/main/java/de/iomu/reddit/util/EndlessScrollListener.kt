package de.iomu.reddit.util

import android.support.v7.widget.RecyclerView
import com.facebook.litho.widget.RecyclerBinder

class EndlessRecyclerScrollListener(val binder: RecyclerBinder, val visibleThreshold: Int = 5, val onLoadMore: () -> Unit) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var isLoading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = binder.itemCount
        val firstVisibleItem: Int = binder.findFirstVisibleItemPosition()

        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore()
            isLoading = true
        }
    }

    fun reset() {
        previousTotal = 0
        isLoading = false
    }
}
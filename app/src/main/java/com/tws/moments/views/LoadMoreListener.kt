package com.tws.moments.views

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal abstract class LoadMoreListener : RecyclerView.OnScrollListener() {
    private var isSlidingUpward = false
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val manager = recyclerView.layoutManager as LinearLayoutManager? ?: return
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
            val itemCount = manager.itemCount

            if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                onLoadMore()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        isSlidingUpward = dy > 0
    }

    abstract fun onLoadMore()
}

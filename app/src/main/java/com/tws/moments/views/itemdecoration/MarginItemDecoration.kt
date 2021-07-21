package com.tws.moments.views.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MarginItemDecoration(private val orientation: Int, private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemCount = parent.adapter?.itemCount ?: 0
        if (itemCount <= 0) {
            return
        }

        val layoutManager = parent.layoutManager

        val left = if (orientation == RecyclerView.VERTICAL) 0 else margin
        val top = if (orientation == RecyclerView.VERTICAL) margin else 0
        val right = if (orientation == RecyclerView.VERTICAL) 0 else margin
        val bottom = if (orientation == RecyclerView.VERTICAL) margin else 0
        if (layoutManager is LinearLayoutManager) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(left, top, 0, 0)
            } else if (parent.getChildAdapterPosition(view) == itemCount - 1) {
                outRect.set(0, 0, right, bottom)
            }
        }
        if (layoutManager is GridLayoutManager) {
            if (parent.getChildAdapterPosition(view) >= 0 && parent.getChildAdapterPosition(view) < getSpanCount(parent)) {
                outRect.set(left, top, 0, 0)
            }
            if (parent.getChildAdapterPosition(view) >= itemCount - getSpanCount(parent)) {
                outRect.set(0, 0, right, bottom)
            }
        }
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> spanCount = layoutManager.spanCount
            is StaggeredGridLayoutManager -> spanCount = layoutManager
                .spanCount
            is LinearLayoutManager -> spanCount = layoutManager.itemCount
        }
        return spanCount
    }
}

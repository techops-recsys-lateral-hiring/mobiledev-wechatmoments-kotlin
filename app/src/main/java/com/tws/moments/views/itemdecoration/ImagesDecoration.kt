package com.tws.moments.views.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImagesDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
    /**
     * 0,1,2
     * 3,4,5
     * 6,7,8
     *  1,2,4,5,7,8 should top offset
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        var left = 0
        if (spanCount == 2 && parent.getChildAdapterPosition(view) > 1) {
            left = margin
        }
        outRect.set(left, margin, 0, 0)
    }
}

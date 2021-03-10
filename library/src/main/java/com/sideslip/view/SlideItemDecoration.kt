package com.sideslip.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * created by wyu on 2021/3/8.
 */
class SlideItemDecoration(private val mItemPadding: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) outRect[0, 0, mItemPadding] =
                    0
            } else {
                if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) outRect[0, 0, 0] =
                    mItemPadding
            }
        }
        if (layoutManager is GridLayoutManager) {
            outRect[mItemPadding, mItemPadding, mItemPadding] = mItemPadding
        }
    }
}
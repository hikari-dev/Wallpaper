package dev.hikari.wallpaper.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space
        val position = parent.getChildAdapterPosition(view)
        val params =
            view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if (params.spanIndex % 2 != 0) {
            //右边
            outRect.left = space / 2
            outRect.right = space
        } else {
            //左边
            outRect.left = space
            outRect.right = space / 2
        }
        if (position == 0) {
            outRect.top = 0
            outRect.right = 0
            outRect.left = 0
        }
    }
}
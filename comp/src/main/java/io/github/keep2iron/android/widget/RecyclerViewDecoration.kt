package io.github.keep2iron.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *
 */
class RecyclerViewDecoration(context: Context, @ColorRes colorRes: Int, @DimenRes dividerHeightRes: Int) : RecyclerView.ItemDecoration() {

    private val dividerHeight: Int
    private val dividerPaint: Paint = Paint()

    init {
        dividerPaint.color = context.resources.getColor(colorRes)
        dividerHeight = context.resources.getDimensionPixelSize(dividerHeightRes)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + dividerHeight).toFloat()
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }
}
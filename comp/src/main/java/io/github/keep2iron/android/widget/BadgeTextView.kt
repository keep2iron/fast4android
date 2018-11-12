package io.github.keep2iron.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.DimenRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.TypedValue

class BadgeTextView(context: Context) : AppCompatTextView(context) {

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @DimenRes
    var badgeSize: Int = 0

    var badgeColor: Int = 0
        set(value) {
            field = value
            paint.color = ContextCompat.getColor(context, field)
//            paint.color = Color.RED
            invalidate()
        }

    var isBadgeVisibility = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isBadgeVisibility) {
            val radio = resources.getDimension(badgeSize) / 2
            val x = (width / 2 + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)).toInt()
            val y = top + paddingTop
            canvas.drawCircle(x.toFloat(), y.toFloat(), radio.toFloat(), paint)
        }
    }
}

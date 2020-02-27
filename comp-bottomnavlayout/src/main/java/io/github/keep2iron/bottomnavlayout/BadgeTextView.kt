package io.github.keep2iron.bottomnavlayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

open class BadgeTextView(context: Context) : AppCompatTextView(context) {

  private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

  @DimenRes
  var badgeSizeRes: Int = 0

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
      val radio = resources.getDimension(badgeSizeRes) / 2
      val x = (width / 2 + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)).toInt()
      val y = top + paddingTop
      canvas.drawCircle(x.toFloat(), y.toFloat(), radio.toFloat(), paint)
    }
  }
}

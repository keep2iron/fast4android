package io.github.keep2iron.app.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/26 15:51
 *
 * 渐变背景的Layout
 */
class GradientDrawable : Drawable() {
  override fun draw(canvas: Canvas) {
    val paint = Paint()
    Log.e("test", "intrinsicWidth : ${intrinsicWidth} minWidth : ${minimumWidth}")

    canvas.save()
    canvas.clipRect(Rect(100, 100, 300, 300))
    canvas.drawColor(Color.BLUE)//裁剪区域的rect变为蓝色
    canvas.drawRect(Rect(0, 0, 100, 100), paint)//在裁剪的区域之外，不能显示
    canvas.drawCircle(150F, 150F, 50F, paint)//在裁剪区域之内，能显示
    canvas.restore()
  }

  override fun setAlpha(alpha: Int) {
  }

  override fun getOpacity(): Int = PixelFormat.UNKNOWN

  override fun setColorFilter(colorFilter: ColorFilter?) {
  }
}
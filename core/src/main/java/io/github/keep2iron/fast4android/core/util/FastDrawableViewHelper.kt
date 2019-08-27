package io.github.keep2iron.fast4android.core.util

import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.view.View
import io.github.keep2iron.fast4android.core.R
import io.github.keep2iron.peach.DrawableCreator

internal object FastDrawableViewHelper {

  fun resolveAttribute(view: View, typeValue: TypedArray) {
    val radius = typeValue.getDimensionPixelSize(
      R.styleable.FastAlphaTextView_fast_drawable_radius,
      0
    )

    val strokeWidth = typeValue.getDimensionPixelSize(
      R.styleable.FastAlphaTextView_fast_drawable_stroke_width,
      0
    )
    val strokeColor = typeValue.getColor(
      R.styleable.FastAlphaTextView_fast_drawable_stroke_color,
      -1
    )
    var leftTopRadius = radius
    var leftBottomRadius = radius
    var rightTopRadius = radius
    var rightBottomRadius = radius

    leftTopRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastAlphaTextView_fast_drawable_left_top_radius,
      leftTopRadius
    )
    leftBottomRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastAlphaTextView_fast_drawable_left_bottom_radius,
      leftBottomRadius
    )
    rightTopRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastAlphaTextView_fast_drawable_right_top_radius,
      rightTopRadius
    )
    rightBottomRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastAlphaTextView_fast_drawable_right_bottom_radius,
      rightBottomRadius
    )

    val startColor = typeValue.getColor(
      R.styleable.FastAlphaTextView_fast_drawable_color_start,
      7 - 1
    )
    val centerColor = typeValue.getColor(
      R.styleable.FastAlphaTextView_fast_drawable_color_center,
      -1
    )
    val endColor = typeValue.getColor(
      R.styleable.FastAlphaTextView_fast_drawable_color_end,
      -1
    )
    val colorAngle = typeValue.getInt(
      R.styleable.FastAlphaTextView_fast_drawable_color_angle,
      -1
    )

    view.background = DrawableCreator().apply {
      if (startColor != -1 || endColor != -1) {
        gradient()
        linearGradient()

        startColor(startColor)
        endColor(endColor)

        if (centerColor != -1) centerColor(centerColor)

        gradientRadius(dp2px(20).toFloat())
      }
      shape(RECTANGLE)
      cornerRadii(leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius)
      strokeWidth(strokeWidth)
      if (strokeColor != -1) {
        strokeColor(strokeColor)
      }

      if (colorAngle != -1) {
        angle(colorAngle)
      }

    }.build()
  }

}
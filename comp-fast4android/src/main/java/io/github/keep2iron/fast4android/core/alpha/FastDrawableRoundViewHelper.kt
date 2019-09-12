package io.github.keep2iron.fast4android.core.alpha

import android.content.Context
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.util.AttributeSet
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.peach.DrawableCreator

class FastDrawableRoundViewHelper {

  var radiusAdjust = false

  fun resolveAttribute(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ): DrawableCreator? {

    val typeValue =
      context.obtainStyledAttributes(
        attrs,
        R.styleable.FastDrawableRoundViewHelper,
        defStyleAttr,
        0
      )

    val haveRadius =
      typeValue.hasValue(R.styleable.FastDrawableRoundViewHelper_fast_drawable_radius) ||
          typeValue.hasValue(R.styleable.FastDrawableRoundViewHelper_fast_drawable_left_top_radius) ||
          typeValue.hasValue(R.styleable.FastDrawableRoundViewHelper_fast_drawable_left_bottom_radius) ||
          typeValue.hasValue(R.styleable.FastDrawableRoundViewHelper_fast_drawable_right_top_radius) ||
          typeValue.hasValue(R.styleable.FastDrawableRoundViewHelper_fast_drawable_right_bottom_radius)

    val radius = typeValue.getDimensionPixelSize(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_radius,
      0
    )

    val strokeWidth = typeValue.getDimensionPixelSize(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_stroke_width,
      0
    )

    val strokeColor = typeValue.getColor(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_stroke_color,
      -1
    )

    var leftTopRadius = radius
    var leftBottomRadius = radius
    var rightTopRadius = radius
    var rightBottomRadius = radius

    leftTopRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_left_top_radius,
      leftTopRadius
    )
    leftBottomRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_left_bottom_radius,
      leftBottomRadius
    )
    rightTopRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_right_top_radius,
      rightTopRadius
    )
    rightBottomRadius = typeValue.getDimensionPixelSize(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_right_bottom_radius,
      rightBottomRadius
    )

    val startColor = typeValue.getColor(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_color_start,
      -1
    )
    val centerColor = typeValue.getColor(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_color_center,
      -1
    )
    val endColor = typeValue.getColor(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_color_end,
      -1
    )
    val colorAngle = typeValue.getInt(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_color_angle,
      -1
    )

    radiusAdjust = typeValue.getBoolean(
      R.styleable.FastDrawableRoundViewHelper_fast_drawable_radius_adjust_bounds,
      false
    )
    if (haveRadius) {
      //如果有圆角则覆盖radiusAdjust属性
      radiusAdjust = false
    }

    if (strokeWidth == 0 && startColor == -1 && endColor == -1) {
      return null
    }

    val drawableCreator = DrawableCreator().apply {
      if (startColor != -1 && endColor != -1) {
        gradient()
        linearGradient()

        startColor(startColor)
        endColor(endColor)

        if (centerColor != -1) centerColor(centerColor)
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
    }

    typeValue.recycle()
    return drawableCreator
  }

}
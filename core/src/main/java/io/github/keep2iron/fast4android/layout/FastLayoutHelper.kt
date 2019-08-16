/*
 * Tencent is pleased to support the open source community by making Fast_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.keep2iron.fast4android.layout

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import io.github.keep2iron.fast4android.comp.R
import io.github.keep2iron.fast4android.util.getAttrDimen
import io.github.keep2iron.fast4android.util.getAttrFloatValue
import java.lang.ref.WeakReference

/**
 * @author cginechen
 * @date 2017-03-10
 */
class FastLayoutHelper(
  private val mContext: Context,
  attrs: AttributeSet?,
  defAttr: Int,
  owner: View
) : IFastLayout {
  // size
  private var mWidthLimit = 0
  private var mHeightLimit = 0
  private var mWidthMini = 0
  private var mHeightMini = 0

  // divider
  private var mTopDividerHeight = 0
  private var mTopDividerInsetLeft = 0
  private var mTopDividerInsetRight = 0
  private var mTopDividerColor: Int = 0
  private var mTopDividerAlpha = 255

  private var mBottomDividerHeight = 0
  private var mBottomDividerInsetLeft = 0
  private var mBottomDividerInsetRight = 0
  private var mBottomDividerColor: Int = 0
  private var mBottomDividerAlpha = 255

  private var mLeftDividerWidth = 0
  private var mLeftDividerInsetTop = 0
  private var mLeftDividerInsetBottom = 0
  private var mLeftDividerColor: Int = 0
  private var mLeftDividerAlpha = 255

  private var mRightDividerWidth = 0
  private var mRightDividerInsetTop = 0
  private var mRightDividerInsetBottom = 0
  private var mRightDividerColor: Int = 0
  private var mRightDividerAlpha = 255
  private var mDividerPaint: Paint? = null

  // round
  private val mClipPaint: Paint
  private val mMode: PorterDuffXfermode
  override var radius: Int = 0
    set(value) {
      if (field != radius) {
        setRadiusAndShadow(radius, shadowElevation, shadowAlpha)
      }
      field = value
    }

  @IFastLayout.HideRadiusSide override var hideRadiusSide = IFastLayout.HIDE_RADIUS_SIDE_NONE
    set(value) {
      if (field == hideRadiusSide) {
        return
      }
      setRadiusAndShadow(
        radius,
        hideRadiusSide, shadowElevation, shadowAlpha
      )
      field = value
    }

  private var mRadiusArray: FloatArray? = null
  private val mBorderRect: RectF
  private var mBorderColor = 0
  private var mBorderWidth = 1
  private var mOuterNormalColor = 0
  private val mOwner: WeakReference<View> = WeakReference(owner)
  private var mIsOutlineExcludePadding = false
  private val mPath = Path()

  // shadow
  private var mIsShowBorderOnlyBeforeL = true
  override var shadowElevation = 0
    set(value) {
      if (field == value) {
        return
      }
      field = value
      invalidate()
    }

  override var shadowAlpha: Float = 0f
    set(value) {
      if (field == value) {
        return
      }
      field = value
      invalidate()
    }

  override var shadowColor = Color.BLACK
    set(value) {
      if (field == value) {
        return
      }
      field = value
      invalidate()
    }

  // outline inset
  private var mOutlineInsetLeft = 0
  private var mOutlineInsetRight = 0
  private var mOutlineInsetTop = 0
  private var mOutlineInsetBottom = 0

  /**
   * 有radius, 但是有一边不显示radius。
   *
   * @return
   */
  val isRadiusWithSideHidden: Boolean
    get() = radius > 0 && hideRadiusSide != IFastLayout.HIDE_RADIUS_SIDE_NONE

  init {
    mTopDividerColor = ContextCompat.getColor(mContext, R.color.fast_config_color_separator)
    mBottomDividerColor = mTopDividerColor
    mMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    mClipPaint = Paint()
    mClipPaint.isAntiAlias = true
    shadowAlpha = mContext.getAttrFloatValue(R.attr.fast_general_shadow_alpha)
    mBorderRect = RectF()

    var radius = 0
    var shadow = 0
    var useThemeGeneralShadowElevation = false
    if (null != attrs || defAttr != 0) {
      val ta = mContext.obtainStyledAttributes(attrs, R.styleable.FastLayout, defAttr, 0)
      val count = ta.indexCount
      for (i in 0 until count) {
        val index = ta.getIndex(i)
        when (index) {
          R.styleable.FastLayout_android_maxWidth -> mWidthLimit =
            ta.getDimensionPixelSize(index, mWidthLimit)
          R.styleable.FastLayout_android_maxHeight -> mHeightLimit =
            ta.getDimensionPixelSize(index, mHeightLimit)
          R.styleable.FastLayout_android_minWidth -> mWidthMini =
            ta.getDimensionPixelSize(index, mWidthMini)
          R.styleable.FastLayout_android_minHeight -> mHeightMini =
            ta.getDimensionPixelSize(index, mHeightMini)
          R.styleable.FastLayout_fast_topDividerColor -> mTopDividerColor =
            ta.getColor(index, mTopDividerColor)
          R.styleable.FastLayout_fast_topDividerHeight -> mTopDividerHeight =
            ta.getDimensionPixelSize(index, mTopDividerHeight)
          R.styleable.FastLayout_fast_topDividerInsetLeft -> mTopDividerInsetLeft =
            ta.getDimensionPixelSize(index, mTopDividerInsetLeft)
          R.styleable.FastLayout_fast_topDividerInsetRight -> mTopDividerInsetRight =
            ta.getDimensionPixelSize(index, mTopDividerInsetRight)
          R.styleable.FastLayout_fast_bottomDividerColor -> mBottomDividerColor =
            ta.getColor(index, mBottomDividerColor)
          R.styleable.FastLayout_fast_bottomDividerHeight -> mBottomDividerHeight =
            ta.getDimensionPixelSize(index, mBottomDividerHeight)
          R.styleable.FastLayout_fast_bottomDividerInsetLeft -> mBottomDividerInsetLeft =
            ta.getDimensionPixelSize(index, mBottomDividerInsetLeft)
          R.styleable.FastLayout_fast_bottomDividerInsetRight -> mBottomDividerInsetRight =
            ta.getDimensionPixelSize(index, mBottomDividerInsetRight)
          R.styleable.FastLayout_fast_leftDividerColor -> mLeftDividerColor =
            ta.getColor(index, mLeftDividerColor)
          R.styleable.FastLayout_fast_leftDividerWidth -> mLeftDividerWidth =
            ta.getDimensionPixelSize(index, mBottomDividerHeight)
          R.styleable.FastLayout_fast_leftDividerInsetTop -> mLeftDividerInsetTop =
            ta.getDimensionPixelSize(index, mLeftDividerInsetTop)
          R.styleable.FastLayout_fast_leftDividerInsetBottom -> mLeftDividerInsetBottom =
            ta.getDimensionPixelSize(index, mLeftDividerInsetBottom)
          R.styleable.FastLayout_fast_rightDividerColor -> mRightDividerColor =
            ta.getColor(index, mRightDividerColor)
          R.styleable.FastLayout_fast_rightDividerWidth -> mRightDividerWidth =
            ta.getDimensionPixelSize(index, mRightDividerWidth)
          R.styleable.FastLayout_fast_rightDividerInsetTop -> mRightDividerInsetTop =
            ta.getDimensionPixelSize(index, mRightDividerInsetTop)
          R.styleable.FastLayout_fast_rightDividerInsetBottom -> mRightDividerInsetBottom =
            ta.getDimensionPixelSize(index, mRightDividerInsetBottom)
          R.styleable.FastLayout_fast_borderColor -> mBorderColor = ta.getColor(index, mBorderColor)
          R.styleable.FastLayout_fast_borderWidth -> mBorderWidth =
            ta.getDimensionPixelSize(index, mBorderWidth)
          R.styleable.FastLayout_fast_radius -> radius = ta.getDimensionPixelSize(index, 0)
          R.styleable.FastLayout_fast_outerNormalColor -> mOuterNormalColor =
            ta.getColor(index, mOuterNormalColor)
          R.styleable.FastLayout_fast_hideRadiusSide -> hideRadiusSide =
            ta.getColor(index, hideRadiusSide)
          R.styleable.FastLayout_fast_showBorderOnlyBeforeL -> mIsShowBorderOnlyBeforeL =
            ta.getBoolean(index, mIsShowBorderOnlyBeforeL)
          R.styleable.FastLayout_fast_shadowElevation -> shadow =
            ta.getDimensionPixelSize(index, shadow)
          R.styleable.FastLayout_fast_shadowAlpha -> shadowAlpha = ta.getFloat(
            index,
            shadowAlpha
          )
          R.styleable.FastLayout_fast_useThemeGeneralShadowElevation -> useThemeGeneralShadowElevation =
            ta.getBoolean(index, false)
          R.styleable.FastLayout_fast_outlineInsetLeft -> mOutlineInsetLeft =
            ta.getDimensionPixelSize(index, 0)
          R.styleable.FastLayout_fast_outlineInsetRight -> mOutlineInsetRight =
            ta.getDimensionPixelSize(index, 0)
          R.styleable.FastLayout_fast_outlineInsetTop -> mOutlineInsetTop =
            ta.getDimensionPixelSize(index, 0)
          R.styleable.FastLayout_fast_outlineInsetBottom -> mOutlineInsetBottom =
            ta.getDimensionPixelSize(index, 0)
          R.styleable.FastLayout_fast_outlineExcludePadding -> mIsOutlineExcludePadding =
            ta.getBoolean(index, false)
        }
      }
      ta.recycle()
    }
    if (shadow == 0 && useThemeGeneralShadowElevation) {
      shadow = mContext.getAttrDimen(R.attr.fast_general_shadow_elevation)

    }
    setRadiusAndShadow(radius, hideRadiusSide, shadow, shadowAlpha)
  }

  override fun setUseThemeGeneralShadowElevation() {
    shadowElevation = mContext.getAttrDimen(R.attr.fast_general_shadow_elevation)
    setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
  }

  override fun setOutlineExcludePadding(outlineExcludePadding: Boolean) {
    if (useFeature()) {
      val owner = mOwner.get() ?: return
      mIsOutlineExcludePadding = outlineExcludePadding
      owner.invalidateOutline()
    }

  }

  override fun setWidthLimit(widthLimit: Int): Boolean {
    if (mWidthLimit != widthLimit) {
      mWidthLimit = widthLimit
      return true
    }
    return false
  }

  override fun setHeightLimit(heightLimit: Int): Boolean {
    if (mHeightLimit != heightLimit) {
      mHeightLimit = heightLimit
      return true
    }
    return false
  }

//  fun getShadowAlpha(): Float {
//    return shadowAlpha
//  }

//  fun getShadowColor(): Int {
//    return shadowColor
//  }

  override fun setOutlineInset(left: Int, top: Int, right: Int, bottom: Int) {
    if (useFeature()) {
      val owner = mOwner.get() ?: return
      mOutlineInsetLeft = left
      mOutlineInsetRight = right
      mOutlineInsetTop = top
      mOutlineInsetBottom = bottom
      owner.invalidateOutline()
    }
  }

  override fun setShowBorderOnlyBeforeL(showBorderOnlyBeforeL: Boolean) {
    mIsShowBorderOnlyBeforeL = showBorderOnlyBeforeL
    invalidate()
  }

//  fun setShadowElevation(elevation: Int) {
//    if (shadowElevation == elevation) {
//      return
//    }
//    shadowElevation = elevation
//    invalidate()
//  }

//  override fun setShadowAlpha(shadowAlpha: Float) {
//    if (shadowAlpha == shadowAlpha) {
//      return
//    }
//    shadowAlpha = shadowAlpha
//    invalidate()
//  }

//  override fun setShadowColor(shadowColor: Int) {
//    if (shadowColor == shadowColor) {
//      return
//    }
//    shadowColor = shadowColor
//    setShadowColorInner(shadowColor)
//  }

  private fun setShadowColorInner(shadowColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      val owner = mOwner.get() ?: return
      owner.outlineAmbientShadowColor = shadowColor
      owner.outlineSpotShadowColor = shadowColor
    }
  }

  private fun invalidate() {
    if (useFeature()) {
      val owner = mOwner.get() ?: return
      if (shadowElevation == 0) {
        owner.elevation = 0f
      } else {
        owner.elevation = shadowElevation.toFloat()
      }
      owner.invalidateOutline()
    }
  }

//  override fun setHideRadiusSide(@HideRadiusSide hideRadiusSide: Int) {
//    if (hideRadiusSide == hideRadiusSide) {
//      return
//    }
//    setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
//  }
//
//  override fun getHideRadiusSide(): Int {
//    return hideRadiusSide
//  }

//  override fun setRadius(radius: Int) {
//    if (radius != radius) {
//      setRadiusAndShadow(radius, shadowElevation, shadowAlpha)
//    }
//  }

  override fun setRadius(radius: Int, @IFastLayout.HideRadiusSide hideRadiusSide: Int) {
    if (this.radius == radius && hideRadiusSide == this.hideRadiusSide) {
      return
    }
    setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
  }

//  override fun getRadius(): Int {
//    return radius
//  }

  override fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float) {
    setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
  }

  override fun setRadiusAndShadow(
    radius: Int, @IFastLayout.HideRadiusSide hideRadiusSide: Int,
    shadowElevation: Int,
    shadowAlpha: Float
  ) {
    setRadiusAndShadow(radius, hideRadiusSide, shadowElevation,
      shadowColor, shadowAlpha)
  }

  override fun setRadiusAndShadow(
    radius: Int,
    hideRadiusSide: Int,
    shadowElevation: Int,
    shadowColor: Int,
    shadowAlpha: Float
  ) {
    val owner = mOwner.get() ?: return

    this.radius = radius
    this.hideRadiusSide = hideRadiusSide

    if (this.radius > 0) {
      if (hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_TOP) {
        mRadiusArray = floatArrayOf(
          0f,
          0f,
          0f,
          0f,
          this.radius.toFloat(),
          this.radius.toFloat(),
          this.radius.toFloat(),
          this.radius.toFloat()
        )
      } else if (hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_RIGHT) {
        mRadiusArray = floatArrayOf(
          this.radius.toFloat(),
          this.radius.toFloat(),
          0f,
          0f,
          0f,
          0f,
          this.radius.toFloat(),
          this.radius.toFloat()
        )
      } else if (hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_BOTTOM) {
        mRadiusArray = floatArrayOf(
          this.radius.toFloat(),
          this.radius.toFloat(),
          this.radius.toFloat(),
          this.radius.toFloat(),
          0f,
          0f,
          0f,
          0f
        )
      } else if (hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_LEFT) {
        mRadiusArray = floatArrayOf(
          0f,
          0f,
          this.radius.toFloat(),
          this.radius.toFloat(),
          this.radius.toFloat(),
          this.radius.toFloat(),
          0f,
          0f
        )
      } else {
        mRadiusArray = null
      }
    }

    this.shadowElevation = shadowElevation
    this.shadowAlpha = shadowAlpha
    this.shadowColor = shadowColor
    if (useFeature()) {
      if (this.shadowElevation == 0 || isRadiusWithSideHidden) {
        owner.elevation = 0f
      } else {
        owner.elevation = this.shadowElevation.toFloat()
      }

      setShadowColorInner(this.shadowColor)

      owner.outlineProvider = object : ViewOutlineProvider() {
        @TargetApi(21)
        override fun getOutline(view: View, outline: Outline) {
          val w = view.width
          val h = view.height
          if (w == 0 || h == 0) {
            return
          }
          if (isRadiusWithSideHidden) {
            var left = 0
            var top = 0
            var right = w
            var bottom = h
            if (this@FastLayoutHelper.hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_LEFT) {
              left -= this@FastLayoutHelper.radius
            } else if (this@FastLayoutHelper.hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_TOP) {
              top -= this@FastLayoutHelper.radius
            } else if (this@FastLayoutHelper.hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_RIGHT) {
              right += this@FastLayoutHelper.radius
            } else if (this@FastLayoutHelper.hideRadiusSide == IFastLayout.HIDE_RADIUS_SIDE_BOTTOM) {
              bottom += this@FastLayoutHelper.radius
            }
            outline.setRoundRect(
              left, top,
              right, bottom, this@FastLayoutHelper.radius.toFloat()
            )
            return
          }

          var top = mOutlineInsetTop
          var bottom = Math.max(top + 1, h - mOutlineInsetBottom)
          var left = mOutlineInsetLeft
          var right = w - mOutlineInsetRight
          if (mIsOutlineExcludePadding) {
            left += view.paddingLeft
            top += view.paddingTop
            right = Math.max(left + 1, right - view.paddingRight)
            bottom = Math.max(top + 1, bottom - view.paddingBottom)
          }

          var shadowAlpha = this@FastLayoutHelper.shadowAlpha
          if (this@FastLayoutHelper.shadowElevation == 0) {
            // outline.setAlpha will work even if shadowElevation == 0
            shadowAlpha = 1f
          }

          outline.alpha = shadowAlpha

          if (this@FastLayoutHelper.radius <= 0) {
            outline.setRect(
              left, top,
              right, bottom
            )
          } else {
            outline.setRoundRect(
              left, top,
              right, bottom, this@FastLayoutHelper.radius.toFloat()
            )
          }
        }
      }
      owner.clipToOutline = this.radius > 0

    }
    owner.invalidate()
  }

  override fun updateTopDivider(
    topInsetLeft: Int,
    topInsetRight: Int,
    topDividerHeight: Int,
    topDividerColor: Int
  ) {
    mTopDividerInsetLeft = topInsetLeft
    mTopDividerInsetRight = topInsetRight
    mTopDividerHeight = topDividerHeight
    mTopDividerColor = topDividerColor
  }

  override fun updateBottomDivider(
    bottomInsetLeft: Int,
    bottomInsetRight: Int,
    bottomDividerHeight: Int,
    bottomDividerColor: Int
  ) {
    mBottomDividerInsetLeft = bottomInsetLeft
    mBottomDividerInsetRight = bottomInsetRight
    mBottomDividerColor = bottomDividerColor
    mBottomDividerHeight = bottomDividerHeight
  }

  override fun updateLeftDivider(
    leftInsetTop: Int,
    leftInsetBottom: Int,
    leftDividerWidth: Int,
    leftDividerColor: Int
  ) {
    mLeftDividerInsetTop = leftInsetTop
    mLeftDividerInsetBottom = leftInsetBottom
    mLeftDividerWidth = leftDividerWidth
    mLeftDividerColor = leftDividerColor
  }

  override fun updateRightDivider(
    rightInsetTop: Int,
    rightInsetBottom: Int,
    rightDividerWidth: Int,
    rightDividerColor: Int
  ) {
    mRightDividerInsetTop = rightInsetTop
    mRightDividerInsetBottom = rightInsetBottom
    mRightDividerWidth = rightDividerWidth
    mRightDividerColor = rightDividerColor
  }

  override fun onlyShowTopDivider(
    topInsetLeft: Int, topInsetRight: Int,
    topDividerHeight: Int, topDividerColor: Int
  ) {
    updateTopDivider(topInsetLeft, topInsetRight, topDividerHeight, topDividerColor)
    mLeftDividerWidth = 0
    mRightDividerWidth = 0
    mBottomDividerHeight = 0
  }

  override fun onlyShowBottomDivider(
    bottomInsetLeft: Int, bottomInsetRight: Int,
    bottomDividerHeight: Int, bottomDividerColor: Int
  ) {
    updateBottomDivider(bottomInsetLeft, bottomInsetRight, bottomDividerHeight, bottomDividerColor)
    mLeftDividerWidth = 0
    mRightDividerWidth = 0
    mTopDividerHeight = 0
  }

  override fun onlyShowLeftDivider(
    leftInsetTop: Int,
    leftInsetBottom: Int,
    leftDividerWidth: Int,
    leftDividerColor: Int
  ) {
    updateLeftDivider(leftInsetTop, leftInsetBottom, leftDividerWidth, leftDividerColor)
    mRightDividerWidth = 0
    mTopDividerHeight = 0
    mBottomDividerHeight = 0
  }

  override fun onlyShowRightDivider(
    rightInsetTop: Int,
    rightInsetBottom: Int,
    rightDividerWidth: Int,
    rightDividerColor: Int
  ) {
    updateRightDivider(rightInsetTop, rightInsetBottom, rightDividerWidth, rightDividerColor)
    mLeftDividerWidth = 0
    mTopDividerHeight = 0
    mBottomDividerHeight = 0
  }

  override fun setTopDividerAlpha(dividerAlpha: Int) {
    mTopDividerAlpha = dividerAlpha
  }

  override fun setBottomDividerAlpha(dividerAlpha: Int) {
    mBottomDividerAlpha = dividerAlpha
  }

  override fun setLeftDividerAlpha(dividerAlpha: Int) {
    mLeftDividerAlpha = dividerAlpha
  }

  override fun setRightDividerAlpha(dividerAlpha: Int) {
    mRightDividerAlpha = dividerAlpha
  }

  fun handleMiniWidth(widthMeasureSpec: Int, measuredWidth: Int): Int {
    return if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY && measuredWidth < mWidthMini) {
      View.MeasureSpec.makeMeasureSpec(mWidthMini, View.MeasureSpec.EXACTLY)
    } else widthMeasureSpec
  }

  fun handleMiniHeight(heightMeasureSpec: Int, measuredHeight: Int): Int {
    return if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY && measuredHeight < mHeightMini) {
      View.MeasureSpec.makeMeasureSpec(mHeightMini, View.MeasureSpec.EXACTLY)
    } else heightMeasureSpec
  }

  fun getMeasuredWidthSpec(widthMeasureSpec: Int): Int {
    var widthMeasureSpec = widthMeasureSpec
    if (mWidthLimit > 0) {
      val size = View.MeasureSpec.getSize(widthMeasureSpec)
      if (size > mWidthLimit) {
        val mode = View.MeasureSpec.getMode(widthMeasureSpec)
        if (mode == View.MeasureSpec.AT_MOST) {
          widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.AT_MOST)
        } else {
          widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.EXACTLY)
        }

      }
    }
    return widthMeasureSpec
  }

  fun getMeasuredHeightSpec(heightMeasureSpec: Int): Int {
    var heightMeasureSpec = heightMeasureSpec
    if (mHeightLimit > 0) {
      val size = View.MeasureSpec.getSize(heightMeasureSpec)
      if (size > mHeightLimit) {
        val mode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (mode == View.MeasureSpec.AT_MOST) {
          heightMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.AT_MOST)
        } else {
          heightMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.EXACTLY)
        }
      }
    }
    return heightMeasureSpec
  }

  override fun setBorderColor(@ColorInt borderColor: Int) {
    mBorderColor = borderColor
  }

  override fun setBorderWidth(borderWidth: Int) {
    mBorderWidth = borderWidth
  }

  override fun setOuterNormalColor(color: Int) {
    mOuterNormalColor = color
    val owner = mOwner.get()
    owner?.invalidate()
  }

  fun drawDividers(canvas: Canvas, w: Int, h: Int) {
    if (mDividerPaint == null && (mTopDividerHeight > 0 || mBottomDividerHeight > 0 || mLeftDividerWidth > 0 || mRightDividerWidth > 0)) {
      mDividerPaint = Paint()
    }
    if (mTopDividerHeight > 0) {
      mDividerPaint!!.strokeWidth = mTopDividerHeight.toFloat()
      mDividerPaint!!.color = mTopDividerColor
      if (mTopDividerAlpha < 255) {
        mDividerPaint!!.alpha = mTopDividerAlpha
      }
      val y = mTopDividerHeight * 1f / 2
      canvas.drawLine(
        mTopDividerInsetLeft.toFloat(),
        y,
        (w - mTopDividerInsetRight).toFloat(),
        y,
        mDividerPaint!!
      )
    }

    if (mBottomDividerHeight > 0) {
      mDividerPaint!!.strokeWidth = mBottomDividerHeight.toFloat()
      mDividerPaint!!.color = mBottomDividerColor
      if (mBottomDividerAlpha < 255) {
        mDividerPaint!!.alpha = mBottomDividerAlpha
      }
      val y = Math.floor((h - mBottomDividerHeight * 1f / 2).toDouble()).toFloat()
      canvas.drawLine(
        mBottomDividerInsetLeft.toFloat(),
        y,
        (w - mBottomDividerInsetRight).toFloat(),
        y,
        mDividerPaint!!
      )
    }

    if (mLeftDividerWidth > 0) {
      mDividerPaint!!.strokeWidth = mLeftDividerWidth.toFloat()
      mDividerPaint!!.color = mLeftDividerColor
      if (mLeftDividerAlpha < 255) {
        mDividerPaint!!.alpha = mLeftDividerAlpha
      }
      canvas.drawLine(
        0f,
        mLeftDividerInsetTop.toFloat(),
        0f,
        (h - mLeftDividerInsetBottom).toFloat(),
        mDividerPaint!!
      )
    }

    if (mRightDividerWidth > 0) {
      mDividerPaint!!.strokeWidth = mRightDividerWidth.toFloat()
      mDividerPaint!!.color = mRightDividerColor
      if (mRightDividerAlpha < 255) {
        mDividerPaint!!.alpha = mRightDividerAlpha
      }
      canvas.drawLine(
        w.toFloat(),
        mRightDividerInsetTop.toFloat(),
        w.toFloat(),
        (h - mRightDividerInsetBottom).toFloat(),
        mDividerPaint!!
      )
    }
  }

  fun dispatchRoundBorderDraw(canvas: Canvas) {
    val owner = mOwner.get() ?: return
    if (mBorderColor == 0 && (radius == 0 || mOuterNormalColor == 0)) {
      return
    }

    if (mIsShowBorderOnlyBeforeL && useFeature() && shadowElevation != 0) {
      return
    }

    val width = canvas.width
    val height = canvas.height

    // react
    if (mIsOutlineExcludePadding) {
      mBorderRect.set(
        (1 + owner.paddingLeft).toFloat(), (1 + owner.paddingTop).toFloat(),
        (width - 1 - owner.paddingRight).toFloat(), (height - 1 - owner.paddingBottom).toFloat()
      )
    } else {
      mBorderRect.set(1f, 1f, (width - 1).toFloat(), (height - 1).toFloat())
    }

    if (radius == 0 || !useFeature() && mOuterNormalColor == 0) {
      mClipPaint.style = Paint.Style.STROKE
      mClipPaint.color = mBorderColor
      canvas.drawRect(mBorderRect, mClipPaint)
      return
    }

    // 圆角矩形
    if (!useFeature()) {
      val layerId =
        canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
      canvas.drawColor(mOuterNormalColor)
      mClipPaint.color = mOuterNormalColor
      mClipPaint.style = Paint.Style.FILL
      mClipPaint.xfermode = mMode
      if (mRadiusArray == null) {
        canvas.drawRoundRect(mBorderRect, radius.toFloat(), radius.toFloat(), mClipPaint)
      } else {
        drawRoundRect(canvas, mBorderRect, mRadiusArray!!, mClipPaint)
      }
      mClipPaint.xfermode = null
      canvas.restoreToCount(layerId)
    }

    mClipPaint.color = mBorderColor
    mClipPaint.strokeWidth = mBorderWidth.toFloat()
    mClipPaint.style = Paint.Style.STROKE
    if (mRadiusArray == null) {
      canvas.drawRoundRect(mBorderRect, radius.toFloat(), radius.toFloat(), mClipPaint)
    } else {
      drawRoundRect(canvas, mBorderRect, mRadiusArray!!, mClipPaint)
    }
  }

  private fun drawRoundRect(canvas: Canvas, rect: RectF, radiusArray: FloatArray, paint: Paint) {
    mPath.reset()
    mPath.addRoundRect(rect, radiusArray, Path.Direction.CW)
    canvas.drawPath(mPath, paint)

  }

  companion object {

    fun useFeature(): Boolean {
      return Build.VERSION.SDK_INT >= 21
    }
  }
}

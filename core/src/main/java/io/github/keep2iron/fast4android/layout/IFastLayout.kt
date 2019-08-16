/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
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

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * Created by cgspine on 2018/3/23.
 */

interface IFastLayout {

  /**
   * See [View.getElevation]
   *
   * @return
   */
  /**
   * See [android.view.View.setElevation]
   *
   * @param elevation
   */
  var shadowElevation: Int

  /**
   * get the outline alpha we set
   *
   * @return
   */
  /**
   * set the outline alpha, which will change the shadow
   *
   * @param shadowAlpha
   */
  var shadowAlpha: Float

  /**
   *
   * @return opaque color
   */
  /**
   *
   * @param shadowColor opaque color
   * @return
   */
  var shadowColor: Int

  /**
   * get the layout radius
   * @return
   */
  /**
   * set the layout radius
   * @param radius
   */
  var radius: Int

  /**
   * get the side that we have hidden the radius
   *
   * @return
   */
  /**
   * in some case, we maybe hope the layout only have radius in one side.
   * but there is no convenient way to write the code like canvas.drawPath,
   * so we take another way that hide one radius side
   *
   * @param hideRadiusSide
   */
  var hideRadiusSide: Int

  @IntDef(value = [HIDE_RADIUS_SIDE_NONE, HIDE_RADIUS_SIDE_TOP, HIDE_RADIUS_SIDE_RIGHT, HIDE_RADIUS_SIDE_BOTTOM, HIDE_RADIUS_SIDE_LEFT])
  @kotlin.annotation.Retention(SOURCE)
  annotation class HideRadiusSide

  /**
   * limit the width of a layout
   *
   * @param widthLimit
   * @return
   */
  fun setWidthLimit(widthLimit: Int): Boolean

  /**
   * limit the height of a layout
   *
   * @param heightLimit
   * @return
   */
  fun setHeightLimit(heightLimit: Int): Boolean

  /**
   * use the shadow elevation from the theme
   */
  fun setUseThemeGeneralShadowElevation()

  /**
   * determine if the outline contain the padding area, usually false
   *
   * @param outlineExcludePadding
   */
  fun setOutlineExcludePadding(outlineExcludePadding: Boolean)

  /**
   * set the layout radius with one or none side been hidden
   * @param radius
   * @param hideRadiusSide
   */
  fun setRadius(radius: Int, @HideRadiusSide hideRadiusSide: Int)

  /**
   * inset the outline if needed
   *
   * @param left
   * @param top
   * @param right
   * @param bottom
   */
  fun setOutlineInset(left: Int, top: Int, right: Int, bottom: Int)

  /**
   * the shadow elevation only work after L, so we provide a downgrading compatible solutions for android 4.x
   * usually we use border, but the border may be redundant for android L+. so will not show border default,
   * if your designer like the border exists with shadow, you can call setShowBorderOnlyBeforeL(false)
   *
   * @param showBorderOnlyBeforeL
   */
  fun setShowBorderOnlyBeforeL(showBorderOnlyBeforeL: Boolean)

  /**
   * this method will determine the radius and shadow.
   *
   * @param radius
   * @param shadowElevation
   * @param shadowAlpha
   */
  fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float)

  /**
   * this method will determine the radius and shadow with one or none side be hidden
   *
   * @param radius
   * @param hideRadiusSide
   * @param shadowElevation
   * @param shadowAlpha
   */
  fun setRadiusAndShadow(
    radius: Int, @HideRadiusSide hideRadiusSide: Int,
    shadowElevation: Int,
    shadowAlpha: Float
  )

  /**
   * this method will determine the radius and shadow (support shadowColor if after android 9)with one or none side be hidden
   *
   * @param radius
   * @param hideRadiusSide
   * @param shadowElevation
   * @param shadowColor
   * @param shadowAlpha
   */
  fun setRadiusAndShadow(
    radius: Int, @HideRadiusSide hideRadiusSide: Int,
    shadowElevation: Int,
    shadowColor: Int,
    shadowAlpha: Float
  )

  /**
   * border color, if you don not set it, the layout will not draw the border
   *
   * @param borderColor
   */
  fun setBorderColor(@ColorInt borderColor: Int)

  /**
   * border width, default is 1px, usually no need to set
   *
   * @param borderWidth
   */
  fun setBorderWidth(borderWidth: Int)

  /**
   * config the top divider
   *
   * @param topInsetLeft
   * @param topInsetRight
   * @param topDividerHeight
   * @param topDividerColor
   */
  fun updateTopDivider(
    topInsetLeft: Int,
    topInsetRight: Int,
    topDividerHeight: Int,
    topDividerColor: Int
  )

  /**
   * config the bottom divider
   *
   * @param bottomInsetLeft
   * @param bottomInsetRight
   * @param bottomDividerHeight
   * @param bottomDividerColor
   */
  fun updateBottomDivider(
    bottomInsetLeft: Int,
    bottomInsetRight: Int,
    bottomDividerHeight: Int,
    bottomDividerColor: Int
  )

  /**
   * config the left divider
   *
   * @param leftInsetTop
   * @param leftInsetBottom
   * @param leftDividerWidth
   * @param leftDividerColor
   */
  fun updateLeftDivider(
    leftInsetTop: Int,
    leftInsetBottom: Int,
    leftDividerWidth: Int,
    leftDividerColor: Int
  )

  /**
   * config the right divider
   *
   * @param rightInsetTop
   * @param rightInsetBottom
   * @param rightDividerWidth
   * @param rightDividerColor
   */
  fun updateRightDivider(
    rightInsetTop: Int,
    rightInsetBottom: Int,
    rightDividerWidth: Int,
    rightDividerColor: Int
  )

  /**
   * show top divider, and hide others
   *
   * @param topInsetLeft
   * @param topInsetRight
   * @param topDividerHeight
   * @param topDividerColor
   */
  fun onlyShowTopDivider(
    topInsetLeft: Int,
    topInsetRight: Int,
    topDividerHeight: Int,
    topDividerColor: Int
  )

  /**
   * show bottom divider, and hide others
   *
   * @param bottomInsetLeft
   * @param bottomInsetRight
   * @param bottomDividerHeight
   * @param bottomDividerColor
   */
  fun onlyShowBottomDivider(
    bottomInsetLeft: Int,
    bottomInsetRight: Int,
    bottomDividerHeight: Int,
    bottomDividerColor: Int
  )

  /**
   * show left divider, and hide others
   *
   * @param leftInsetTop
   * @param leftInsetBottom
   * @param leftDividerWidth
   * @param leftDividerColor
   */
  fun onlyShowLeftDivider(
    leftInsetTop: Int,
    leftInsetBottom: Int,
    leftDividerWidth: Int,
    leftDividerColor: Int
  )

  /**
   * show right divider, and hide others
   *
   * @param rightInsetTop
   * @param rightInsetBottom
   * @param rightDividerWidth
   * @param rightDividerColor
   */
  fun onlyShowRightDivider(
    rightInsetTop: Int,
    rightInsetBottom: Int,
    rightDividerWidth: Int,
    rightDividerColor: Int
  )

  /**
   * after config the border, sometimes we need change the alpha of divider with animation,
   * so we provide a method to individually change the alpha
   *
   * @param dividerAlpha [0, 255]
   */
  fun setTopDividerAlpha(dividerAlpha: Int)

  /**
   * @param dividerAlpha [0, 255]
   */
  fun setBottomDividerAlpha(dividerAlpha: Int)

  /**
   * @param dividerAlpha [0, 255]
   */
  fun setLeftDividerAlpha(dividerAlpha: Int)

  /**
   * @param dividerAlpha [0, 255]
   */
  fun setRightDividerAlpha(dividerAlpha: Int)

  /**
   * only available before android L
   * @param color
   */
  fun setOuterNormalColor(color: Int)

  companion object {
    const val HIDE_RADIUS_SIDE_NONE = 0
    const val HIDE_RADIUS_SIDE_TOP = 1
    const val HIDE_RADIUS_SIDE_RIGHT = 2
    const val HIDE_RADIUS_SIDE_BOTTOM = 3
    const val HIDE_RADIUS_SIDE_LEFT = 4
  }

}

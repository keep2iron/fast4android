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

package io.github.keep2iron.fast4android.core.layout

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntDef

/**
 * Created by cgspine on 2018/3/23.
 */

interface IFastLayout {

    companion object {
        const val HIDE_RADIUS_SIDE_LEFT_TOP = 1
        const val HIDE_RADIUS_SIDE_RIGHT_TOP = 2
        const val HIDE_RADIUS_SIDE_LEFT_BOTTOM = 3
        const val HIDE_RADIUS_SIDE_RIGHT_BOTTOM = 4

        const val DIRECTION_LEFT = 1
        const val DIRECTION_TOP = 2
        const val DIRECTION_RIGHT = 3
        const val DIRECTION_BOTTOM = 4
    }

    @IntDef(
            value = [
                HIDE_RADIUS_SIDE_LEFT_TOP,
                HIDE_RADIUS_SIDE_RIGHT_TOP,
                HIDE_RADIUS_SIDE_LEFT_BOTTOM,
                HIDE_RADIUS_SIDE_RIGHT_BOTTOM
            ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class HideRadiusCorner

    @IntDef(
            value = [
                DIRECTION_LEFT,
                DIRECTION_TOP,
                DIRECTION_RIGHT,
                DIRECTION_BOTTOM
            ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Direction

    /**
     * limit the width of a layoutInflate
     *
     * @param widthLimit
     * @return
     */
    fun setWidthLimit(widthLimit: Int): Boolean

    /**
     * limit the height of a layoutInflate
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
     * See {@link android.view.View#setElevation(float)}
     *
     * @param elevation
     */
    fun setShadowElevation(elevation: Int)

    /**
     * See {@link View#getElevation()}
     *
     * @return
     */
    fun getShadowElevation(): Int

    /**
     * set the outline alpha, which will change the shadow
     *
     * @param shadowAlpha
     */
    fun setShadowAlpha(@FloatRange(from = 0.0, to = 1.0) shadowAlpha: Float)

    /**
     * get the outline alpha we set
     *
     * @return
     */
    @FloatRange(from = 0.0, to = 1.0)
    fun getShadowAlpha(): Float

    /**
     *
     * @param shadowColor opaque color
     * @return
     */
    fun setShadowColor(@ColorInt shadowColor: Int)

    /**
     *
     * @return opaque color
     */
    @ColorInt
    fun getShadowColor(): Int

    /**
     * set the layoutInflate radius
     * @param radius
     */
    fun setRadius(radius: Int)

    /**
     * set the layoutInflate radius with one or none side been hidden
     * @param radius
     * @param hideRadiusCorner
     */
    fun setRadius(radius: Int, @HideRadiusCorner vararg hideRadiusCorner: Int)

    /**
     * get the layoutInflate radius
     * @return
     */
    fun getRadius(): Int

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
     * get the side that we have hidden the radius
     *
     * leftTop rightTop rightBottom leftBottom
     *
     * @return true is hide ,false is show
     */
    fun getHideRadiusSide(): BooleanArray

    /**
     * this method will determine the radius and shadow.
     *
     * @param radius
     * @param shadowElevation
     * @param shadowColor
     * @param shadowAlpha
     */
    fun setRadiusAndShadow(
            radius: Int,
            shadowElevation: Int,
            @ColorInt shadowColor: Int = 0,
            shadowAlpha: Float
    )

//  /**
//   * this method will determine the radius and shadow with one or none side be hidden
//   *
//   * @param radius
//   * @param hideRadiusSide
//   * @param shadowElevation
//   * @param shadowAlpha
//   */
//  fun setRadiusAndShadow(
//    radius: Int,
//    @HideRadiusCorner vararg hideRadiusSide: Int,
//    shadowElevation: Int,
//    @ColorInt shadowColor: Int = 0,
//    shadowAlpha: Float
//  )

    /**
     * border color, if you don not set it, the layoutInflate will not draw the border
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

    fun setDivider(
            dividerSize: Int,
            insetStart: Int,
            insetEnd: Int,
            @ColorInt dividerColor: Int,
            @Direction vararg direction: Int
    )

    /**
     * after config the border, sometimes we need change the alpha of divider with animation,
     * so we provide a method to individually change the alpha
     *
     * @param dividerAlpha [0, 1]
     */
    fun setDividerAlpha(
            @FloatRange(
                    from = 0.0,
                    to = 1.0
            ) dividerAlpha: Float, @Direction vararg direction: Int
    )

    /**
     * only available before android L
     * @param color
     */
    fun setOuterNormalColor(@ColorInt color: Int)

}

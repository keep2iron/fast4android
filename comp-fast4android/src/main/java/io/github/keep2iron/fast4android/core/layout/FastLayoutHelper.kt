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

package io.github.keep2iron.fast4android.core.layout

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import io.github.keep2iron.base.util.getAttrColor
import io.github.keep2iron.base.util.getAttrDimen
import io.github.keep2iron.base.util.getAttrFloatValue
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.fast4android.core.layout.IFastLayout.HideRadiusCorner
import java.lang.ref.WeakReference
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.math.max

class FastLayoutParams(
        // size
        var widthLimit: Int = 0,
        var heightLimit: Int = 0,
        var widthMini: Int = 0,
        var heightMini: Int = 0,

        //divider
        val defaultDividerColor: Int,
        private var dividerPaint: Paint? = null,

        var radius: Int = 0,
        //leftTop rightTop rightBottom leftBottom
        var hideRadiusCornerArray: BooleanArray = BooleanArray(4) { true },

        var radiusArray: FloatArray = FloatArray(8),
        val borderRect: RectF = RectF(),
        @ColorInt var borderColor: Int = 0,
        var borderWidth: Int = 1,
        var outerNormalColor: Int = 0,
        var isOutlineExcludePadding: Boolean = false,
        // shadow
        var isShowBorderOnlyBeforeL: Boolean = true,
        var shadowAlpha: Float = 0f,
        var shadowElevation: Int = 0,
        @ColorInt var shadowColor: Int = Color.BLACK,
        // outline inset
        var outlineInsetLeft: Int = 0,
        var outlineInsetRight: Int = 0,
        var outlineInsetTop: Int = 0,
        var outlineInsetBottom: Int = 0
) {
    // divider
    class DividerProperties(
            var dividerSize: Int = 0,
            var dividerInsetStart: Int = 0,
            var dividerInsetEnd: Int = 0,
            var dividerColor: Int = 0,
            //[0,1]
            var dividerAlpha: Float = 1f
    )

    //left top right bottom
    val dividers by lazy(NONE) {
        arrayListOf(
                DividerProperties(),
                DividerProperties(),
                DividerProperties(),
                DividerProperties()
        )
    }

    val path: Path by lazy(NONE) {
        Path()
    }

}

class FastLayoutHelper(
        private val mContext: Context,
        attrs: AttributeSet?,
        defAttr: Int,
        owner: View
) : IFastLayout {

    private var mDividerPaint: Paint? = null

    private val clipPaint = Paint()

    private val mOwner: WeakReference<View> = WeakReference(owner)

    private val params: FastLayoutParams =
            FastLayoutParams(
                    defaultDividerColor = ContextCompat.getColor(
                            mContext,
                            R.color.fast_config_color_separator
                    )
            )

    /**
     * 有radius, 但是有一边不显示radius。
     *
     * @return
     */
    val isRadiusCornerHidden: Boolean
        get() = params.radius > 0 && params.hideRadiusCornerArray.count { it } > 0

    private val defaultDividerColor = mContext.getAttrColor(R.attr.fast_config_color_separator)

    init {
        clipPaint.isAntiAlias = true
        params.shadowAlpha = mContext.getAttrFloatValue(R.attr.fast_general_shadow_alpha)

        var radius = 0
        var shadowElevation = 0
        var useThemeGeneralShadowElevation = false
        if (null != attrs || defAttr != 0) {
            val ta = mContext.obtainStyledAttributes(attrs, R.styleable.FastLayout, defAttr, 0)
            val count = ta.indexCount
            for (i in 0 until count) {
                when (val index = ta.getIndex(i)) {
                    R.styleable.FastLayout_android_maxWidth -> params.widthLimit =
                            ta.getDimensionPixelSize(index, params.widthLimit)
                    R.styleable.FastLayout_android_maxHeight -> params.heightLimit =
                            ta.getDimensionPixelSize(index, params.heightLimit)
                    R.styleable.FastLayout_android_minWidth -> params.widthMini =
                            ta.getDimensionPixelSize(index, params.widthMini)
                    R.styleable.FastLayout_android_minHeight -> params.heightMini =
                            ta.getDimensionPixelSize(index, params.heightMini)
                    //left
                    R.styleable.FastLayout_fast_leftDividerColor -> params.dividers[0].dividerColor =
                            ta.getColor(index, params.defaultDividerColor)
                    R.styleable.FastLayout_fast_leftDividerWidth -> params.dividers[0].dividerSize =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_leftDividerInsetTop -> params.dividers[0].dividerInsetStart =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_leftDividerInsetBottom -> params.dividers[0].dividerInsetEnd =
                            ta.getDimensionPixelSize(index, 0)
                    //top
                    R.styleable.FastLayout_fast_topDividerColor -> params.dividers[1].dividerColor =
                            ta.getColor(index, defaultDividerColor)
                    R.styleable.FastLayout_fast_topDividerHeight -> params.dividers[1].dividerSize =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_topDividerInsetLeft -> params.dividers[1].dividerInsetStart =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_topDividerInsetRight -> params.dividers[1].dividerInsetEnd =
                            ta.getDimensionPixelSize(index, 0)
                    //right
                    R.styleable.FastLayout_fast_rightDividerColor -> params.dividers[2].dividerColor =
                            ta.getColor(index, defaultDividerColor)
                    R.styleable.FastLayout_fast_rightDividerWidth -> params.dividers[2].dividerSize =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_rightDividerInsetTop -> params.dividers[2].dividerInsetStart =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_rightDividerInsetBottom -> params.dividers[2].dividerInsetEnd =
                            ta.getDimensionPixelSize(index, 0)
                    //bottom
                    R.styleable.FastLayout_fast_bottomDividerColor -> params.dividers[3].dividerColor =
                            ta.getColor(index, defaultDividerColor)
                    R.styleable.FastLayout_fast_bottomDividerHeight -> params.dividers[3].dividerSize =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_bottomDividerInsetLeft -> params.dividers[3].dividerInsetStart =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_bottomDividerInsetRight -> params.dividers[3].dividerInsetEnd =
                            ta.getDimensionPixelSize(index, 0)
                    //border
                    R.styleable.FastLayout_fast_borderColor -> params.borderColor =
                            ta.getColor(index, params.borderColor)
                    R.styleable.FastLayout_fast_borderWidth -> params.borderWidth =
                            ta.getDimensionPixelSize(index, params.borderWidth)
                    //radius
                    R.styleable.FastLayout_fast_radius -> radius = ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_outerNormalColor -> params.outerNormalColor =
                            ta.getColor(index, params.outerNormalColor)
                    R.styleable.FastLayout_fast_hideRadiusSide -> {
                        when (ta.getInt(index, 0)) {
                            IFastLayout.HIDE_RADIUS_SIDE_LEFT_TOP -> {
                                params.hideRadiusCornerArray[0] = false
                            }
                            IFastLayout.HIDE_RADIUS_SIDE_RIGHT_TOP -> {
                                params.hideRadiusCornerArray[1] = false
                            }
                            IFastLayout.HIDE_RADIUS_SIDE_RIGHT_BOTTOM -> {
                                params.hideRadiusCornerArray[2] = false
                            }
                            IFastLayout.HIDE_RADIUS_SIDE_LEFT_BOTTOM -> {
                                params.hideRadiusCornerArray[3] = false
                            }
                        }
                    }
                    R.styleable.FastLayout_fast_showBorderOnlyBeforeL -> params.isShowBorderOnlyBeforeL =
                            ta.getBoolean(index, params.isShowBorderOnlyBeforeL)
                    R.styleable.FastLayout_fast_shadowElevation -> shadowElevation =
                            ta.getDimensionPixelSize(index, shadowElevation)
                    R.styleable.FastLayout_fast_shadowAlpha -> params.shadowAlpha = ta.getFloat(
                            index,
                            params.shadowAlpha
                    )
                    R.styleable.FastLayout_fast_useThemeGeneralShadowElevation -> useThemeGeneralShadowElevation =
                            ta.getBoolean(index, false)
                    R.styleable.FastLayout_fast_outlineInsetLeft -> params.outlineInsetLeft =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_outlineInsetRight -> params.outlineInsetRight =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_outlineInsetTop -> params.outlineInsetTop =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_outlineInsetBottom -> params.outlineInsetBottom =
                            ta.getDimensionPixelSize(index, 0)
                    R.styleable.FastLayout_fast_outlineExcludePadding -> params.isOutlineExcludePadding =
                            ta.getBoolean(index, false)
                }
            }
            ta.recycle()
        }
        if (shadowElevation == 0 && useThemeGeneralShadowElevation) {
            shadowElevation = mContext.getAttrDimen(R.attr.fast_general_shadow_elevation)
        }
        setRadiusAndShadow(
                radius = radius,
                shadowElevation = shadowElevation,
                shadowAlpha = params.shadowAlpha
        )
    }

    override fun setWidthLimit(widthLimit: Int): Boolean {
        if (params.widthLimit != widthLimit) return false
        else params.widthLimit = widthLimit
        return true
    }

    override fun setHeightLimit(heightLimit: Int): Boolean {
        if (params.heightLimit != heightLimit) return false
        else params.heightLimit = heightLimit
        return true
    }

    override fun setUseThemeGeneralShadowElevation() {
        params.shadowElevation = mContext.getAttrDimen(R.attr.fast_general_shadow_elevation)
        setRadiusAndShadow(
                radius = params.radius,
                shadowElevation = params.shadowElevation,
                shadowColor = params.shadowColor,
                shadowAlpha = params.shadowAlpha
        )
    }

    override fun setOutlineExcludePadding(outlineExcludePadding: Boolean) {
        if (useFeature()) {
            val owner = mOwner.get() ?: return
            params.isOutlineExcludePadding = outlineExcludePadding
            owner.invalidateOutline()
        }
    }

    override fun setShadowElevation(elevation: Int) {
        if (params.shadowElevation == elevation) {
            return
        }
        params.shadowElevation = elevation
        invalidate()
    }

    override fun getShadowElevation(): Int = params.shadowElevation

    override fun setShadowAlpha(shadowAlpha: Float) {
        if (params.shadowAlpha == shadowAlpha) {
            return
        }
        params.shadowAlpha = shadowAlpha
        invalidate()
    }

    override fun getShadowAlpha(): Float = params.shadowAlpha

    override fun setShadowColor(@ColorInt shadowColor: Int) {
        if (params.shadowColor == shadowColor) {
            return
        }
        params.shadowColor = shadowColor
        setShadowColorInner(shadowColor)
    }

    private fun setShadowColorInner(shadowColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val owner = mOwner.get() ?: return
            owner.outlineAmbientShadowColor = shadowColor
            owner.outlineSpotShadowColor = shadowColor
        }
    }

    override fun getShadowColor(): Int = params.shadowColor

    override fun setRadius(radius: Int) {
        params.radius = radius
        params.hideRadiusCornerArray.fill(false)

        setRadiusAndShadow(
                params.radius,
                params.shadowElevation,
                params.shadowColor,
                params.shadowAlpha
        )
    }

    override fun setRadius(radius: Int, @HideRadiusCorner vararg hideRadiusCorner: Int) {
//    if (params.radius == radius && hideRadiusCorner.isEmpty()) return
        var changed = false

        hideRadiusCorner.forEach { hideCorner ->
            when (hideCorner) {
                IFastLayout.HIDE_RADIUS_SIDE_LEFT_TOP -> {
                    if (!params.hideRadiusCornerArray[0]) {
                        params.hideRadiusCornerArray[0] = true
                        changed = true
                    }
                }
                IFastLayout.HIDE_RADIUS_SIDE_RIGHT_TOP -> {
                    if (!params.hideRadiusCornerArray[1]) {
                        params.hideRadiusCornerArray[1] = true
                        changed = true
                    }
                }
                IFastLayout.HIDE_RADIUS_SIDE_RIGHT_BOTTOM -> {
                    if (!params.hideRadiusCornerArray[2]) {
                        params.hideRadiusCornerArray[2] = true
                        changed = true
                    }
                }
                IFastLayout.HIDE_RADIUS_SIDE_LEFT_BOTTOM -> {
                    if (!params.hideRadiusCornerArray[3]) {
                        params.hideRadiusCornerArray[3] = true
                        changed = true
                    }
                }
            }
        }

        if (changed) {
            setRadiusAndShadow(
                    params.radius,
                    params.shadowElevation,
                    params.shadowColor,
                    params.shadowAlpha
            )
        }
    }

    override fun getRadius(): Int = params.radius

    override fun setOutlineInset(left: Int, top: Int, right: Int, bottom: Int) {
        if (useFeature()) {
            val view = mOwner.get() ?: return
            params.outlineInsetLeft = left
            params.outlineInsetTop = top
            params.outlineInsetRight = right
            params.outlineInsetBottom = bottom
            view.invalidateOutline()
        }
    }

    override fun setShowBorderOnlyBeforeL(showBorderOnlyBeforeL: Boolean) {
        if (params.isShowBorderOnlyBeforeL != showBorderOnlyBeforeL) {
            params.isShowBorderOnlyBeforeL = showBorderOnlyBeforeL
        }
        invalidate()
    }

    override fun getHideRadiusSide(): BooleanArray = params.hideRadiusCornerArray

    override fun setRadiusAndShadow(
            radius: Int,
            shadowElevation: Int,
            shadowColor: Int,
            shadowAlpha: Float
    ) {
        val view = mOwner.get() ?: return
        params.radius = radius
        params.shadowElevation = shadowElevation
        params.shadowColor = shadowColor
        params.shadowAlpha = shadowAlpha
        params.radiusArray.fill(radius.toFloat())

        params.hideRadiusCornerArray.forEachIndexed { index, hide ->
            if (hide) {
                params.radiusArray[index * 2] = 0f
                params.radiusArray[index * 2 + 1] = 0f
            }
        }
        if (useFeature()) {
            if (params.shadowElevation == 0 || isRadiusCornerHidden) {
                view.elevation = 0f
            } else {
                view.elevation = params.shadowElevation.toFloat()
            }

            setShadowColorInner(params.shadowColor)

            view.outlineProvider =
                    InnerViewProvider(this)
            view.clipToOutline = params.radius > 0
        }
        view.invalidate()
    }

    override fun setBorderColor(borderColor: Int) {
        params.borderColor = borderColor
    }

    override fun setBorderWidth(borderWidth: Int) {
        params.borderWidth = borderWidth
    }

    override fun setDivider(
            dividerSize: Int,
            insetStart: Int,
            insetEnd: Int,
            dividerColor: Int,
            vararg direction: Int
    ) {
        direction.forEach { itemDirection ->
            when (itemDirection) {
                IFastLayout.DIRECTION_LEFT -> {
                    params.dividers[0].dividerSize = dividerSize
                    params.dividers[0].dividerInsetStart = insetStart
                    params.dividers[0].dividerInsetEnd = insetEnd
                    params.dividers[0].dividerColor = dividerColor
                }
                IFastLayout.DIRECTION_TOP -> {
                    params.dividers[1].dividerSize = dividerSize
                    params.dividers[1].dividerInsetStart = insetStart
                    params.dividers[1].dividerInsetEnd = insetEnd
                    params.dividers[1].dividerColor = dividerColor
                }
                IFastLayout.DIRECTION_RIGHT -> {
                    params.dividers[2].dividerSize = dividerSize
                    params.dividers[2].dividerInsetStart = insetStart
                    params.dividers[2].dividerInsetEnd = insetEnd
                    params.dividers[2].dividerColor = dividerColor
                }
                IFastLayout.DIRECTION_BOTTOM -> {
                    params.dividers[3].dividerSize = dividerSize
                    params.dividers[3].dividerInsetStart = insetStart
                    params.dividers[3].dividerInsetEnd = insetEnd
                    params.dividers[3].dividerColor = dividerColor
                }
            }
        }
    }

    override fun setDividerAlpha(
            @FloatRange(
                    from = 0.0,
                    to = 1.0
            ) dividerAlpha: Float, vararg direction: Int
    ) {
        direction.forEach { itemDirection ->
            when (itemDirection) {
                IFastLayout.DIRECTION_LEFT -> {
                    params.dividers[0].dividerAlpha = dividerAlpha
                }
                IFastLayout.DIRECTION_TOP -> {
                    params.dividers[1].dividerAlpha = dividerAlpha
                }
                IFastLayout.DIRECTION_RIGHT -> {
                    params.dividers[2].dividerAlpha = dividerAlpha
                }
                IFastLayout.DIRECTION_BOTTOM -> {
                    params.dividers[3].dividerAlpha = dividerAlpha
                }
            }
        }
    }

    override fun setOuterNormalColor(color: Int) {
        params.outerNormalColor = color
        val view = mOwner.get() ?: return
        view.invalidate()
    }

    private fun invalidate() {
        if (useFeature()) {
            val owner = mOwner.get() ?: return
            owner.elevation = params.shadowElevation.toFloat()
            owner.invalidateOutline()
        }
    }

    companion object {
        fun useFeature(): Boolean {
            return Build.VERSION.SDK_INT >= 21
        }
    }

    @RequiresApi(VERSION_CODES.LOLLIPOP)
    private class InnerViewProvider(val helper: FastLayoutHelper) : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            if (view.width == 0 || view.height == 0) {
                return
            }
            if (view.visibility != View.VISIBLE) {
                return
            }

            if (helper.isRadiusCornerHidden) {
                var left = 0
                var top = 0
                var right = view.width
                var bottom = view.height
                when {
                    helper.params.hideRadiusCornerArray[0] -> left -= helper.params.radius
                    helper.params.hideRadiusCornerArray[1] -> top -= helper.params.radius
                    helper.params.hideRadiusCornerArray[2] -> right -= helper.params.radius
                    helper.params.hideRadiusCornerArray[3] -> bottom -= helper.params.radius
                }
                outline.setRoundRect(left, top, right, bottom, helper.params.radius.toFloat())
                return
            }

            var top = helper.params.outlineInsetTop
            var bottom = max(top + 1, view.height - helper.params.outlineInsetBottom)
            var left = helper.params.outlineInsetLeft
            var right = view.width - helper.params.outlineInsetRight
            if (helper.params.isOutlineExcludePadding) {
                left += view.paddingLeft
                top += view.paddingTop
                right = max(left + 1, right - view.paddingRight)
                bottom = max(top + 1, bottom - view.paddingBottom)
            }

            if (helper.params.shadowElevation == 0) {
                // outline.setAlpha will work even if shadowElevation == 0
                helper.params.shadowAlpha = 1f
            }

            outline.alpha = helper.params.shadowAlpha

            if (helper.params.radius <= 0) {
                outline.setRect(
                        left, top,
                        right, bottom
                )
            } else {
                outline.setRoundRect(
                        left, top,
                        right, bottom, helper.params.radius.toFloat()
                )
            }
        }
    }
}

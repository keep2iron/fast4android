package io.github.keep2iron.fast4android.core.alpha

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import io.github.keep2iron.fast4android.core.layout.FastLayoutHelper
import io.github.keep2iron.fast4android.core.layout.IFastLayout
import kotlin.LazyThreadSafetyMode.NONE

open class FastAlphaFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IFastLayout {

    private val fastAlphaViewHelper by lazy(NONE) {
        FastAlphaViewHelper(this)
    }

    private val fastLayoutHelper: FastLayoutHelper =
            FastLayoutHelper(
                    context,
                    attrs,
                    defStyleAttr,
                    this
            )

    private val fastDrawableViewHelper = FastDrawableRoundViewHelper()

    init {
        background = fastDrawableViewHelper.resolveAttribute(context, attrs, defStyleAttr)?.build()
        setChangeAlphaWhenPress(false)
        setChangeAlphaWhenDisable(false)
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        fastAlphaViewHelper.onPressedChanged(this, pressed)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        fastAlphaViewHelper.onEnabledChanged(this, enabled)
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        fastAlphaViewHelper.setChangeAlphaWhenPress(changeAlphaWhenPress)
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        fastAlphaViewHelper.setChangeAlphaWhenDisable(changeAlphaWhenDisable)
    }

    override fun setWidthLimit(widthLimit: Int): Boolean = fastLayoutHelper.setWidthLimit(widthLimit)

    override fun setHeightLimit(heightLimit: Int): Boolean =
            fastLayoutHelper.setHeightLimit(heightLimit)

    override fun setUseThemeGeneralShadowElevation() {
        fastLayoutHelper.setUseThemeGeneralShadowElevation()
    }

    override fun setOutlineExcludePadding(outlineExcludePadding: Boolean) {
        fastLayoutHelper.setOutlineExcludePadding(outlineExcludePadding)
    }

    override fun setShadowElevation(elevation: Int) {
        fastLayoutHelper.setShadowElevation(elevation)
    }

    override fun getShadowElevation(): Int = fastLayoutHelper.getShadowElevation()

    override fun setShadowAlpha(@FloatRange(from = 0.0, to = 1.0) shadowAlpha: Float) {
        fastLayoutHelper.setShadowAlpha(shadowAlpha)
    }

    @FloatRange(from = 0.0, to = 1.0)
    override fun getShadowAlpha(): Float =
            fastLayoutHelper.getShadowAlpha()

    override fun setShadowColor(shadowColor: Int) {
        fastLayoutHelper.setShadowColor(shadowColor)
    }

    override fun getShadowColor(): Int = fastLayoutHelper.getShadowColor()

    override fun setRadius(radius: Int) {
        fastLayoutHelper.setRadius(radius)
    }

    override fun setRadius(radius: Int, @IFastLayout.HideRadiusCorner vararg hideRadiusCorner: Int) {
        fastLayoutHelper.setRadius(radius, *hideRadiusCorner)
    }

    override fun getRadius(): Int = fastLayoutHelper.getRadius()

    override fun setOutlineInset(left: Int, top: Int, right: Int, bottom: Int) {
        fastLayoutHelper.setOutlineInset(left, top, right, bottom)
    }

    override fun setShowBorderOnlyBeforeL(showBorderOnlyBeforeL: Boolean) {
        fastLayoutHelper.setShowBorderOnlyBeforeL(showBorderOnlyBeforeL)
    }

    override fun getHideRadiusSide(): BooleanArray = fastLayoutHelper.getHideRadiusSide()

    override fun setRadiusAndShadow(
            radius: Int,
            shadowElevation: Int,
            shadowColor: Int,
            shadowAlpha: Float
    ) {
        fastLayoutHelper.setRadiusAndShadow(radius, shadowElevation, shadowColor, shadowAlpha)
    }

    override fun setBorderColor(borderColor: Int) {
        fastLayoutHelper.setBorderColor(borderColor)
    }

    override fun setBorderWidth(borderWidth: Int) {
        fastLayoutHelper.setBorderWidth(borderWidth)
    }

    override fun setDivider(
            dividerSize: Int,
            insetStart: Int,
            insetEnd: Int,
            dividerColor: Int,
            vararg direction: Int
    ) {
        fastLayoutHelper.setDivider(dividerSize, insetStart, insetEnd, dividerColor, *direction)
    }

    override fun setDividerAlpha(dividerAlpha: Float, vararg direction: Int) {
        fastLayoutHelper.setDividerAlpha(dividerAlpha, *direction)
    }

    override fun setOuterNormalColor(color: Int) {
        fastLayoutHelper.setOuterNormalColor(color)
    }
}
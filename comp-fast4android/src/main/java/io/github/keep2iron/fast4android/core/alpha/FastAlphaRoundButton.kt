package io.github.keep2iron.fast4android.core.alpha

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.peach.DrawableCreator

open class FastAlphaRoundButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.FastButtonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val fastAlphaViewHelper = FastAlphaViewHelper(this)

    private val fastDrawableViewHelper =
            FastDrawableRoundViewHelper()

    private var drawableCreator: DrawableCreator?

    init {
        drawableCreator = fastDrawableViewHelper.resolveAttribute(context, attrs, defStyleAttr)

        setChangeAlphaWhenPress(true)
        setChangeAlphaWhenDisable(true)
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val minSize = width.coerceAtMost(height)
        background = if (fastDrawableViewHelper.radiusAdjust) {
            drawableCreator?.cornerRadii(minSize / 2, minSize / 2, minSize / 2, minSize / 2)
            drawableCreator?.build()
        } else {
            drawableCreator?.build()
        }
    }
}
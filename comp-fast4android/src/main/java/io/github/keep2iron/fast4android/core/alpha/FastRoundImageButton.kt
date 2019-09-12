package io.github.keep2iron.fast4android.core.alpha

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.peach.DrawableCreator

open class FastRoundImageButton @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.FastImageButtonStyle
) : AppCompatImageButton(context, attrs, defStyleAttr) {

  private val fastAlphaViewHelper = FastAlphaViewHelper(this)

  private val fastDrawableViewHelper = FastDrawableRoundViewHelper()

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

}
package io.github.keep2iron.fast4android.alpha

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import android.util.AttributeSet
import kotlin.LazyThreadSafetyMode.NONE

class FastAlphaButton @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {


  private val fastAlphaViewHelper by lazy(NONE) {
    FastAlphaViewHelper(this)
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
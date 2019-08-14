package io.github.keep2iron.fast4android.alpha

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import io.github.keep2iron.fast4android.comp.R
import kotlin.LazyThreadSafetyMode.NONE

class FastAlphaTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.FastAlphaTextViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

  private val fastAlphaViewHelper by lazy(NONE) {
    FastAlphaViewHelper(this)
  }

  init {
    val defValue = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics
    ).toInt()

    val typedArray =
      context.obtainStyledAttributes(attrs, R.styleable.FastAlphaTextView, defStyleAttr, 0)
    typedArray.hasValue(R.styleable.FastAlphaTextView_fastDrawableTopHeight)

    setCompoundTextView(
      compoundDrawables[0],
      typedArray,
      defValue,
      R.styleable.FastAlphaTextView_fastDrawableLeftWidth,
      R.styleable.FastAlphaTextView_fastDrawableLeftHeight
    )
    setCompoundTextView(
      compoundDrawables[1],
      typedArray,
      defValue,
      R.styleable.FastAlphaTextView_fastDrawableTopWidth,
      R.styleable.FastAlphaTextView_fastDrawableTopHeight
    )
    setCompoundTextView(
      compoundDrawables[2],
      typedArray,
      defValue,
      R.styleable.FastAlphaTextView_fastDrawableRightWidth,
      R.styleable.FastAlphaTextView_fastDrawableRightHeight
    )
    setCompoundTextView(
      compoundDrawables[3],
      typedArray,
      defValue,
      R.styleable.FastAlphaTextView_fastDrawableBottomWidth,
      R.styleable.FastAlphaTextView_fastDrawableBottomHeight
    )

    setCompoundTint(
      compoundDrawables[0],
      typedArray,
      R.styleable.FastAlphaTextView_fastDrawableLeftTint
    )

    setCompoundTint(
      compoundDrawables[1],
      typedArray,
      R.styleable.FastAlphaTextView_fastDrawableTopTint
    )

    setCompoundTint(
      compoundDrawables[2],
      typedArray,
      R.styleable.FastAlphaTextView_fastDrawableRightTint
    )

    setCompoundTint(
      compoundDrawables[3],
      typedArray,
      R.styleable.FastAlphaTextView_fastDrawableBottomTint
    )

    typedArray.recycle()
  }

  private fun setCompoundTint(
    compoundDrawable: Drawable?,
    typedArray: TypedArray,
    @StyleableRes colorAttr: Int
  ) {
    if (compoundDrawable != null && typedArray.hasValue(colorAttr)) {
      compoundDrawable.setColorFilter(
        typedArray.getColor(colorAttr, Color.TRANSPARENT), PorterDuff.Mode.SRC_ATOP
      )
    }
  }

  private fun setCompoundTextView(
    compoundDrawable: Drawable?,
    typedArray: TypedArray,
    defValue: Int,
    @StyleableRes widthAttr: Int,
    @StyleableRes heightAttr: Int
  ) {
    compoundDrawable?.setBounds(
      0, 0, typedArray.getDimensionPixelSize(
        widthAttr,
        defValue
      )
      ,
      typedArray.getDimensionPixelSize(
        heightAttr,
        defValue
      )
    )
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
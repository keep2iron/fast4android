package io.github.keep2iron.textviewplus

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue

class CompTextViewPlus @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

  init {

    val defValue = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics
    ).toInt()

    val typedArray =
      context.obtainStyledAttributes(attrs, R.styleable.CompTextViewPlus, defStyleAttr, 0)

    val compoundDrawables = compoundDrawables

    if (compoundDrawables[0] != null) {                                //left
      compoundDrawables[0].setBounds(
        0, 0, typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableLeftWidth,
          defValue
        )
        ,
        typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableLeftHeight,
          defValue
        )
      )
    }

    if (compoundDrawables[1] != null) {                                //top
      compoundDrawables[1].setBounds(
        0, 0, typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableTopWidth,
          defValue
        )
        ,
        typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableTopHeight,
          defValue
        )
      )
    }

    if (compoundDrawables[2] != null) {                                //right
      compoundDrawables[2].setBounds(
        0, 0, typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableRightWidth,
          defValue
        )
        ,
        typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableRightHeight,
          defValue
        )
      )
    }

    if (compoundDrawables[3] != null) {                                //bottom
      compoundDrawables[3].setBounds(
        0, 0, typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableBottomWidth,
          defValue
        )
        ,
        typedArray.getDimensionPixelSize(
          R.styleable.CompTextViewPlus_compDrawableBottomHeight,
          defValue
        )
      )
    }

    setCompoundDrawables(
      compoundDrawables[0],
      compoundDrawables[1],
      compoundDrawables[2],
      compoundDrawables[3]
    )

    typedArray.recycle()
  }
}
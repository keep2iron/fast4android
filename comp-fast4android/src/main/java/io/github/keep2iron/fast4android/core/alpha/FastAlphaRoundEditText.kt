package io.github.keep2iron.fast4android.core.alpha

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import io.github.keep2iron.base.util.setPaddingRight
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.fast4android.core.util.dp2px
import kotlin.math.pow
import kotlin.math.sqrt

open class FastAlphaRoundEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val fastAlphaViewHelper = FastAlphaViewHelper(this, 1.0f, 0.8f)

    private val fastDrawableViewHelper =
            FastDrawableRoundViewHelper()

    private val touchPoint = PointF()

    private val drawableRightRect = RectF()

    //点击偏差
    private val DRAWABLE_BIAS = dp2px(5)

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    init {
        val defValue = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 22f, resources.displayMetrics
        ).toInt()

        background = fastDrawableViewHelper.resolveAttribute(context, attrs, defStyleAttr)?.build()

        //setup delete actionView
        if (compoundDrawables[2] == null) {
            this.setCompoundDrawables(
                    compoundDrawables[0],
                    compoundDrawables[1],
                    ContextCompat.getDrawable(context, R.drawable.fast_ic_delete),
                    compoundDrawables[3]
            )
            setPaddingRight(dp2px(6))
        }

        val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.FastAlphaRoundEditText, defStyleAttr, 0)
        setCompoundTextView(
                compoundDrawables[0],
                typedArray,
                defValue,
                R.styleable.FastAlphaRoundTextView_fast_drawableLeftWidth,
                R.styleable.FastAlphaRoundTextView_fast_drawableLeftHeight
        )
        setCompoundTextView(
                compoundDrawables[1],
                typedArray,
                defValue,
                R.styleable.FastAlphaRoundTextView_fast_drawableTopWidth,
                R.styleable.FastAlphaRoundTextView_fast_drawableTopHeight
        )
        setCompoundTextView(
                compoundDrawables[2],
                typedArray,
                defValue,
                R.styleable.FastAlphaRoundTextView_fast_drawableRightWidth,
                R.styleable.FastAlphaRoundTextView_fast_drawableRightHeight
        )
        setCompoundTextView(
                compoundDrawables[3],
                typedArray,
                defValue,
                R.styleable.FastAlphaRoundTextView_fast_drawableBottomWidth,
                R.styleable.FastAlphaRoundTextView_fast_drawableBottomHeight
        )

        setCompoundTint(
                compoundDrawables[0],
                typedArray,
                R.styleable.FastAlphaRoundTextView_fast_drawableLeftTint
        )

        setCompoundTint(
                compoundDrawables[1],
                typedArray,
                R.styleable.FastAlphaRoundTextView_fast_drawableTopTint
        )

        setCompoundTint(
                compoundDrawables[2],
                typedArray,
                R.styleable.FastAlphaRoundTextView_fast_drawableRightTint
        )

        setCompoundTint(
                compoundDrawables[3],
                typedArray,
                R.styleable.FastAlphaRoundTextView_fast_drawableBottomTint
        )

        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                compoundDrawables[2],
                compoundDrawables[3]
        )

        typedArray.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPoint.x = event.x
                touchPoint.y = event.y
            }
            MotionEvent.ACTION_UP -> {
                val dis = sqrt((event.x - touchPoint.x).toDouble().pow(2) + (event.y - touchPoint.y).pow(2))
                if (compoundDrawables[2] != null && dis < dp2px(40) && drawableRightRect.contains(touchPoint.x, touchPoint.y)) {
                    text?.clear()
                }
            }
        }
        return super.onTouchEvent(event)
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        drawableRightRect.set(
                (width - paddingRight - compoundDrawables[2].bounds.width()).toFloat() - DRAWABLE_BIAS,
                height / 2 - compoundDrawables[2].bounds.height() / 2f - DRAWABLE_BIAS,
                (width - paddingRight).toFloat() + DRAWABLE_BIAS,
                height / 2 + compoundDrawables[2].bounds.height() / 2f + DRAWABLE_BIAS
        )
    }


}
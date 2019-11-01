package io.github.keep2iron.fast4android.topbar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import io.github.keep2iron.base.util.FastWindowInsetHelper
import io.github.keep2iron.base.util.setPaddingBottom
import io.github.keep2iron.base.util.setPaddingTop
import io.github.keep2iron.fast4android.core.alpha.FastDrawableRoundViewHelper

class FastTopBarLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.FastTopBarStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    val fastTopBar = FastTopBar(context)

    private val fastDrawableViewHelper = FastDrawableRoundViewHelper()

    init {
        val drawableCreator = fastDrawableViewHelper.resolveAttribute(context, attrs, defStyleAttr)
        fastTopBar.background = drawableCreator?.build() ?: background

        val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.FastTopBarLayout, defStyleAttr, 0)
        fastTopBar.resolveTypedArray(typedArray)
        typedArray.recycle()
        addView(fastTopBar)

        //将TopBar的背景移植到TopBarLayout上
        background = fastTopBar.background
        //取消TopBar的背景颜色
        fastTopBar.background = null

        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            setPaddingTop(insets.systemWindowInsetTop)
            if (insets.systemWindowInsetBottom > FastWindowInsetHelper.KEYBOARD_HEIGHT_BOUNDARY) {
                setPaddingBottom(0)
            } else {
                setPaddingBottom(insets.systemWindowInsetBottom)
            }

            insets
        }
    }

    inline fun setup(block: FastTopBar.() -> Unit) {
        fastTopBar.apply(block)
    }
}
package io.github.keep2iron.fast4android.topbar

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import io.github.keep2iron.base.util.FastWindowInsetHelper
import io.github.keep2iron.base.util.setPaddingBottom
import io.github.keep2iron.base.util.setPaddingTop
import io.github.keep2iron.fast4android.core.alpha.FastDrawableRoundViewHelper
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


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
        addView(fastTopBar, generateDefaultLayoutParams())

        /**
         * 修复4.4
         * https://stackoverflow.com/questions/10095196/whered-padding-go-when-setting-background-drawable
         */
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            val pL = paddingLeft
            val pT = paddingTop
            val pR = paddingRight
            val pB = paddingBottom

            //将TopBar的背景移植到TopBarLayout上
            background = fastTopBar.background
            //取消TopBar的背景颜色
            fastTopBar.background = null

            fastTopBar.setPadding(pL, pT, pR, pB)
            setPadding(0, 0, 0, 0)
        } else {
            setPadding(0, 0, 0, 0)
            //将TopBar的背景移植到TopBarLayout上
            background = fastTopBar.background
            //取消TopBar的背景颜色
            fastTopBar.background = null
        }

        //主要为了适配当键盘弹起 或者当布局占用底部导航栏时的 多出来的insets.bottom
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            //            if (insets.systemWindowInsetBottom > FastWindowInsetHelper.KEYBOARD_HEIGHT_BOUNDARY) {
            this.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, 0)
//            } else {
//                this.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
//            }

            insets
        }
    }

    inline fun setup(block: FastTopBar.() -> Unit) {
        fastTopBar.apply(block)
    }
}
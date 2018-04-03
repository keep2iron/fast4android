package io.github.keep2iron.android.core

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * Created by picher on 2018/2/26.
 * Describe：
 */

class CustomerPopup private constructor(private val mContext: Context) : PopupWindow.OnDismissListener {
    private lateinit var mPopWindows: PopupWindow
    private var mContentView: View? = null
    private var mLayoutId = 0
    //寬高
    private var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    //動畫
    private var mAnimationStyle = 0
    //觸摸外部dismiss
    private var ismOutsideFocusEnable: Boolean = false
    private var mOutsideTouchable: Boolean = false
    private var mFocusable: Boolean = false
    //是否背景變暗
    private var isBackgroundDim = false
    private var mDimView: View? = null

    private var mAnchor: View? = null
    private var mOffsetX: Int = 0
    private var mOffsetY: Int = 0
    /**
     * 透明度
     */
    private val mDimColor = 0x65000000
    private var mOriginColor = Color.WHITE
    private val isColorful = false

    fun <T : CustomerPopup> createPopup(): T {
        mPopWindows = PopupWindow()
        if (mContentView == null) {
            if (mLayoutId != 0) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutId, null)
            } else {
                throw IllegalArgumentException("pop content view is null")
            }
        }

        mPopWindows.contentView = mContentView
        mPopWindows.width = mWidth
        mPopWindows.height = mHeight
        if (mAnimationStyle != 0) {
            mPopWindows.animationStyle = mAnimationStyle
        }

        if (ismOutsideFocusEnable) {
            mPopWindows.isFocusable = mFocusable
            mPopWindows.isOutsideTouchable = mOutsideTouchable
            mPopWindows.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        //設置監聽
        mPopWindows.setOnDismissListener(this)

        return this as T
    }

    fun <T : CustomerPopup> setContentView(mContentView: View): T {
        this.mContentView = mContentView
        return this as T
    }

    fun <T : CustomerPopup> setContentView(mContentView: View, width: Int, height: Int): T {
        this.mContentView = mContentView
        this.mWidth = width
        this.mHeight = height
        return this as T
    }

    fun <T : CustomerPopup> setLayoutId(mLayoutId: Int): T {
        this.mLayoutId = mLayoutId
        return this as T
    }

    fun <T : CustomerPopup> setWidth(mWidth: Int): T {
        this.mWidth = mWidth
        return this as T
    }

    fun <T : CustomerPopup> setHeight(mHeight: Int): T {
        this.mHeight = mHeight
        return this as T
    }

    fun <T : CustomerPopup> setAnimationStyle(mAnimationStyle: Int): T {
        this.mAnimationStyle = mAnimationStyle
        return this as T
    }

    fun <T : CustomerPopup> setIsOutsideFocusEnable(ismOutsideFocusEnable: Boolean): T {
        this.ismOutsideFocusEnable = ismOutsideFocusEnable
        return this as T
    }

    fun <T : CustomerPopup> setOutsideTouchable(mOutsideTouchable: Boolean): T {
        this.mOutsideTouchable = mOutsideTouchable
        return this as T
    }

    fun <T : CustomerPopup> setFoucusable(focusable: Boolean): T {
        this.mFocusable = focusable
        return this as T
    }

    fun <T : CustomerPopup> setBackgroundDim(backgroundDim: Boolean): T {
        isBackgroundDim = backgroundDim
        return this as T
    }

    fun <T : CustomerPopup> setDimView(dimView: View): T {
        mDimView = dimView
        return this as T
    }

    @JvmOverloads
    fun showAsDropDown(anchor: View, offsetX: Int = 0, offsetY: Int = 0) {
        if (mPopWindows != null) {
            this.mAnchor = anchor
            this.mOffsetX = offsetX
            this.mOffsetY = offsetY
            if (isBackgroundDim) {
                changeBackgroundDim(true)
            }
            mPopWindows?.showAsDropDown(anchor, offsetX, offsetY)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun showAsDropDown(anchor: View, offsetX: Int, offsetY: Int, gravity: Int) {
        if (mPopWindows != null) {
            this.mAnchor = anchor
            this.mOffsetX = offsetX
            this.mOffsetY = offsetY
            if (isBackgroundDim) {
                changeBackgroundDim(true)
            }
            mPopWindows?.showAsDropDown(anchor, offsetX, offsetY, gravity)
        }
    }

    private fun changeBackgroundDim(isDark: Boolean) {
        if (mDimView != null) {
            if (isColorful) {
                showDimColorful(isDark)
            } else {
                showDimOverlay(isDark)
            }
        }
    }

    private fun showDimOverlay(isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (isDark) {
                val parent = mDimView as ViewGroup?
                val dim = ColorDrawable(mDimColor)
                parent?.let {
                    dim.alpha = (255 * DEFAULT_TRANSPARENT_VALUE).toInt()
                    dim.setBounds(0, 0, parent.width, parent.height)
                    val overlay = parent.overlay
                    overlay.add(dim)
                }
            } else {
                val parent = mDimView as ViewGroup?
                val overlay = parent?.overlay
                overlay?.clear()
            }
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    @Deprecated("")
    private fun showDimColorful(isDark: Boolean) {
        val colorAnim: ValueAnimator
        if (isDark) {
            val background = mDimView?.background
            if (background is ColorDrawable) {
                mOriginColor = background.color
            }
            colorAnim = ObjectAnimator.ofInt(mDimView, "backgroundColor", mOriginColor, mDimColor)
        } else {
            colorAnim = ObjectAnimator.ofInt(mDimView, "backgroundColor", mDimColor, mOriginColor)
        }
        colorAnim.duration = 300
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
    }

    override fun onDismiss() {
        //背景變亮
        changeBackgroundDim(false)

        if (mPopWindows.isShowing) {
            mPopWindows.dismiss()
        }
    }

    companion object {

        //默认背景透明值
        private const val DEFAULT_TRANSPARENT_VALUE = 0.7f
    }
}

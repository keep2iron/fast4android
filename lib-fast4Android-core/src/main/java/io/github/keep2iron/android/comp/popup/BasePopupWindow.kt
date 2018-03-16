package io.github.keep2iron.android.comp.popup

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager

import io.github.keep2iron.android.util.DisplayUtil

/**
 * copy from qmui
 * 修改自 @author Lorensius W. L. T <lorenz></lorenz>@londatiga.net>
 */
abstract class BasePopupWindow(private var mContext: Context) {
    protected var mWindow: android.widget.PopupWindow? = null
    private var mRootViewWrapper: RootView? = null
    protected lateinit var mRootView: View
    protected var mBackground: Drawable? = null
    protected var mWindowManager: WindowManager
    private var mDismissListener: android.widget.PopupWindow.OnDismissListener? = null

    protected var mScreenSize = Point()
    protected var mWindowHeight = 0
    protected var mWindowWidth = 0

    //cache
    private var mNeedCacheSize = true

    val decorView: View?
        get() {
            var decorView: View? = null
            try {
                if (mWindow!!.background == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        decorView = mWindow!!.contentView.parent as View
                    } else {
                        decorView = mWindow!!.contentView
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        decorView = mWindow!!.contentView.parent.parent as View
                    } else {
                        decorView = mWindow!!.contentView.parent as View
                    }
                }
            } catch (ignore: Exception) {

            }

            return decorView
        }

    val isShowing: Boolean
        get() = mWindow != null && mWindow!!.isShowing

    init {
        mWindow = android.widget.PopupWindow(mContext)
        mWindow!!.setTouchInterceptor(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                mWindow!!.dismiss()
                return@OnTouchListener false
            }
            false
        })

        mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    }

    /**
     * On dismiss
     */
    protected fun onDismiss() {}


    fun dimBehind(dim: Float) {
        if (!isShowing) {
            throw RuntimeException("should call after method show() or in onShowEnd()")
        }
        val decorView = decorView
        if (decorView != null) {
            val p = decorView.layoutParams as WindowManager.LayoutParams
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            p.dimAmount = dim
            mWindowManager.updateViewLayout(decorView, p)
        }
    }

    fun show(view: View) {
        show(view, view)
    }


    fun show(parent: View, anchorView: View) {
        if (!ViewCompat.isAttachedToWindow(anchorView)) {
            return
        }
        onShowConfig()
        if (mWindowWidth == 0 || mWindowHeight == 0 || !mNeedCacheSize) {
            measureWindowSize()
        }

        val point = onShowBegin(parent, anchorView)

        mWindow!!.showAtLocation(parent, Gravity.NO_GRAVITY, point.x, point.y)

        onShowEnd()

        // 在相关的View被移除时，window也自动移除。避免当Fragment退出后，Fragment中弹出的PopupWindow还存在于界面上。
        parent.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                if (isShowing) {
                    dismiss()
                }
            }
        })
    }

    protected fun onShowConfig() {
        if (mRootViewWrapper == null) {
            throw IllegalStateException("setContentView was not called with a view to display.")
        }

        if (mBackground == null) {
            mWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } else {
            mWindow!!.setBackgroundDrawable(mBackground)
        }

        mWindow!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        mWindow!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        mWindow!!.isTouchable = true
        mWindow!!.isFocusable = true
        mWindow!!.isOutsideTouchable = true

        mWindow!!.contentView = mRootViewWrapper

        val screenDisplay = mWindowManager.defaultDisplay
        screenDisplay.getSize(mScreenSize)
    }

    protected abstract fun onShowBegin(parent: View, attachedView: View): Point

    protected fun onShowEnd() {

    }

    private fun measureWindowSize() {
        val widthMeasureSpec = makeWidthMeasureSpec()
        val heightMeasureSpec = makeHeightMeasureSpec()
        mRootView.measure(widthMeasureSpec, heightMeasureSpec)
        mWindowWidth = mRootView.measuredWidth
        mWindowHeight = mRootView.measuredHeight
        Log.i(TAG, "measureWindowSize: mWindowWidth = $mWindowWidth ;mWindowHeight = $mWindowHeight")
    }

    protected fun makeWidthMeasureSpec(): Int {
        return View.MeasureSpec.makeMeasureSpec(DisplayUtil.getScreenWidth(mContext), View.MeasureSpec.AT_MOST)
    }

    protected fun makeHeightMeasureSpec(): Int {
        return View.MeasureSpec.makeMeasureSpec(DisplayUtil.getScreenHeight(mContext), View.MeasureSpec.AT_MOST)
    }


    /**
     * Set background drawable.
     *
     * @param background Background drawable
     */
    fun setBackgroundDrawable(background: Drawable) {
        mBackground = background
    }

    /**
     * Set content view.
     *
     * @param root Root view
     */
    fun setContentView(root: View?) {
        if (root == null) {
            throw IllegalStateException("setContentView was not called with a view to display.")
        }
        mRootViewWrapper = RootView(mContext)
        mRootViewWrapper!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mRootView = root
        mRootViewWrapper!!.addView(root)
        mWindow!!.contentView = mRootViewWrapper
        mWindow!!.setOnDismissListener {
            this@BasePopupWindow.onDismiss()
            if (mDismissListener != null) {
                mDismissListener!!.onDismiss()
            }
        }
    }

    protected abstract fun onWindowSizeChange()


    /**
     * Set content view.
     *
     * @param layoutResID Resource id
     */
    fun setContentView(layoutResID: Int) {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(inflater.inflate(layoutResID, null))
    }

    fun setOnDismissListener(listener: android.widget.PopupWindow.OnDismissListener) {
        mDismissListener = listener
    }

    fun dismiss() {
        mWindow!!.dismiss()
    }

    protected fun onConfigurationChanged(newConfig: Configuration) {

    }

    fun setNeedCacheSize(needCacheSize: Boolean) {
        mNeedCacheSize = needCacheSize
    }

    inner class RootView : ViewGroup {
        constructor(context: Context) : super(context) {}

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

        override fun onConfigurationChanged(newConfig: Configuration) {
            if (mWindow != null && mWindow!!.isShowing) {
                mWindow!!.dismiss()
            }
            this@BasePopupWindow.onConfigurationChanged(newConfig)
        }

        override fun addView(child: View) {
            if (childCount > 0) {
                throw RuntimeException("only support one child")
            }
            super.addView(child)
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            var widthMeasureSpec = widthMeasureSpec
            var heightMeasureSpec = heightMeasureSpec
            if (childCount == 0) {
                setMeasuredDimension(0, 0)
            }
            //            int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
            val parentHeightSize = View.MeasureSpec.getSize(heightMeasureSpec)
            widthMeasureSpec = makeWidthMeasureSpec()
            heightMeasureSpec = makeHeightMeasureSpec()
            //            int targetWidthSize = MeasureSpec.getSize(widthMeasureSpec);
            //            int targetWidthMode = MeasureSpec.getMode(widthMeasureSpec);
            val targetHeightSize = View.MeasureSpec.getSize(heightMeasureSpec)
            val targetHeightMode = View.MeasureSpec.getMode(heightMeasureSpec)
            // fixme why parentWidthSize < screen width ?
            //            if (parentWidthSize < targetWidthSize) {
            //                widthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidthSize, targetWidthMode);
            //            }
            if (parentHeightSize < targetHeightSize) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentHeightSize, targetHeightMode)
            }
            val child = getChildAt(0)
            child.measure(widthMeasureSpec, heightMeasureSpec)
            val oldWidth = mWindowWidth
            val oldHeight = mWindowHeight
            mWindowWidth = child.measuredWidth
            mWindowHeight = child.measuredHeight
            if (oldWidth != mWindowWidth || oldHeight != mWindowHeight && mWindow!!.isShowing) {
                onWindowSizeChange()
            }
            Log.i(TAG, "in measure: mWindowWidth = $mWindowWidth ;mWindowHeight = $mWindowHeight")
            setMeasuredDimension(mWindowWidth, mWindowHeight)
        }

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
            if (childCount == 0) {
                return
            }
            val child = getChildAt(0)
            child.layout(0, 0, child.measuredWidth, child.measuredHeight)
        }
    }

    companion object {
        private val TAG = "QMUIBasePopup"
    }
}
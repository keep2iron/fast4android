package io.github.keep2iron.android.core

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import android.util.Pair
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

import io.github.keep2iron.android.R
import io.github.keep2iron.android.core.rx.LifecycleEvent
import io.github.keep2iron.android.core.rx.RxLifecycle
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/01/29 16:02
 */
abstract class AbstractDialogFragment<T : ViewDataBinding> : DialogFragment() {


    protected var mSubject = BehaviorSubject.create<LifecycleEvent>()

    protected lateinit var mContentView: View

    protected var mDataBinding: T? = null

    /**
     * 映射布局方法
     *
     * @return 被映射的布局id
     */
    @get:LayoutRes
    protected abstract val resId: Int

    val windowAnim: Int
        @StyleRes get() = -1

    /**
     * 初始化方法
     *
     * @param container 被映射的container对象
     */
    abstract fun initVariables(container: View?)

    /**
     * 在初始化方法之前进行调用的方法，子类可以选择性重写
     */
    protected fun beforeInitVariables() {}

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val dialog = InnerDialog(activity!!, R.style.dialog)
        dialog.setContentView(createView())
        dialog.setCanceledOnTouchOutside(true)

        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog.window
        val params = window!!.attributes
        params.gravity = gravity()
        window.attributes = params
        val anim = windowAnim
        if (anim != -1) {
            dialog.window!!.attributes.windowAnimations = anim
        }

        return dialog
    }

    abstract fun gravity(): Int

    private fun createView(): View {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                ?: throw IllegalArgumentException("inflater == null")

        mDataBinding = DataBindingUtil.inflate(inflater, resId, null, false)
        if (mDataBinding == null) {
            mContentView = inflater.inflate(resId, null, false)
        } else {
            mContentView = mDataBinding!!.root
        }

        beforeInitVariables()
        mContentView.isClickable = true

        initVariables(mContentView)

        return mContentView
    }

    fun <V : View> findViewById(@IdRes id: Int): V {
        return mContentView.findViewById(id)
    }

    abstract fun widthAndHeight(): Pair<Int, Int>

    fun <F> bindObservableLifeCycle(): ObservableTransformer<F, F> {
        return RxLifecycle.bindUntilEvent(mSubject, LifecycleEvent.DESTROY)
    }

    override fun onStart() {
        super.onStart()
        mSubject.onNext(LifecycleEvent.START)
    }

    override fun onResume() {
        super.onResume()
        mSubject.onNext(LifecycleEvent.RESUME)

        val dialog = dialog
        val pair = widthAndHeight()
        dialog.window!!.setLayout(pair.first, pair.second)
    }

    override fun onPause() {
        super.onPause()
        mSubject.onNext(LifecycleEvent.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        mSubject.onNext(LifecycleEvent.STOP)
    }

    override fun onDestroy() {
        mSubject.onNext(LifecycleEvent.DESTROY)
        super.onDestroy()
    }

    fun onTouchOutside() {

    }

    internal inner class InnerDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (isCancelable && isShowing && event.action == MotionEvent.ACTION_DOWN && isTouchOutside(event)) {
                this@AbstractDialogFragment.onTouchOutside()
                dismiss()
                return true
            }

            return false
        }

        private fun isTouchOutside(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val slop = ViewConfiguration.get(context).scaledWindowTouchSlop
            val decorView = window!!.decorView
            return (x < -slop || y < -slop
                    || x > decorView.width + slop
                    || y > decorView.height + slop)
        }
    }
}
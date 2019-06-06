package io.github.keep2iron.android.core

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.*
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject
import android.view.WindowManager
import android.view.ViewGroup
import io.github.keep2iron.android.R
import io.github.keep2iron.android.rx.LifecycleEvent
import io.github.keep2iron.android.rx.RxLifecycle


/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/01/29 16:02
 */
abstract class AbstractDialogFragment<DB : ViewDataBinding> : DialogFragment() {

    protected var mSubject: BehaviorSubject<LifecycleEvent> = BehaviorSubject.create<LifecycleEvent>()

    protected lateinit var mContentView: View

    protected lateinit var dataBinding: DB

    protected lateinit var contextHolder: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextHolder = context
    }

    /**
     * 映射布局方法
     *
     * @return 被映射的布局id
     */
    @get:LayoutRes
    protected abstract val resId: Int

    /**
     * 窗体动画
     */
    open fun getWindowAnim(): Int = -1

    /**
     * 初始化方法
     *
     * @param container 被映射的container对象
     */
    abstract fun initVariables(container: View)

    /**
     * 在初始化方法之前进行调用的方法，子类可以选择性重写
     */
    protected fun beforeInitVariables() {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = activity ?: contextHolder

        val dialog = InnerDialog(context, R.style.dialog)
        isCancelable = true
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        Log.d("tag", "onCreateDialog")

        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog.window
        window?.let {
            window.setDimAmount(getBackgroundDimAmount())
            val params = window.attributes
            val width = width()
            if (width != -1) {
                params.width = width
            }
            val height = height()
            if (height != -1) {
                params.height = height
            }
            params.x = offsetX()
            params.y = offsetY()
            params.gravity = gravity()
            window.attributes = params
            val anim = getWindowAnim()
            if (anim != -1) {
                window.attributes.windowAnimations = anim
            }
        }

        return dialog
    }

    open fun width(): Int {
        return -1
    }

    open fun height(): Int {
        return -1
    }

    /**
     * X position for this window.  With the default gravity it is ignored.
     * When using {@link Gravity#LEFT} or {@link Gravity#START} or {@link Gravity#RIGHT} or
     * {@link Gravity#END} it provides an offset from the given edge.
     */
    open fun offsetX(): Int {
        return 0
    }

    /**
     * Y position for this window.  With the default gravity it is ignored.
     * When using {@link Gravity#TOP} or {@link Gravity#BOTTOM} it provides
     * an offset from the given edge.
     */
    open fun offsetY(): Int {
        return 0
    }

    /**
     * 就是用来控制灰度的值，当为1时，界面除了我们的dialog内容是高亮显示的，dialog以外的区域是黑色的，完全看不到其他内容，系统的默认值是0.5
     */
    open fun getBackgroundDimAmount() = 0.35f

    open fun setWidthAndHeight(): Array<Int> {
        return arrayOf(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    abstract fun gravity(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val genDataBinding = DataBindingUtil.inflate<DB>(inflater, resId, null, false)
        mContentView = if (genDataBinding != null) {
            dataBinding = genDataBinding
            dataBinding.root
        } else {
            inflater.inflate(resId, null, false)
        }
        mContentView.fitsSystemWindows = false
        mContentView.isClickable = true
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables(mContentView)
    }

    fun <V : View> findViewById(@IdRes id: Int): V {
        return mContentView.findViewById(id)
    }

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
        val pair = setWidthAndHeight()
        if (pair.size != 2) {
            throw IllegalArgumentException("setWidthAndHeight() must return 2 integer value => [width,height]")
        }
        dialog.window!!.setLayout(pair[0], pair[1])
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

    open fun onTouchOutside() {
    }

    override fun isCancelable(): Boolean {
        return super.isCancelable()
    }

    internal inner class InnerDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

        override fun onTouchEvent(event: MotionEvent): Boolean {
//            Log.d("tag", "isCancelable ${isCancelable} isShowing : ${isShowing} event.action == MotionEvent.ACTION_DOWN ${event.action == MotionEvent.ACTION_DOWN} isTouchOutside(event) : ${isTouchOutside(event)}")
            if (isShowing && event.action == MotionEvent.ACTION_DOWN && isTouchOutside(event)) {
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

//            Log.d("tag", "wdith  : ${decorView.width + slop} height : ${decorView.height + slop}")

            return (x < -slop || y < -slop
                    || x > decorView.width + slop
                    || y > decorView.height + slop)
        }
    }
}
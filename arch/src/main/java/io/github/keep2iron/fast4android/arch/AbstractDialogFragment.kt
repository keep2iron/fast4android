package io.github.keep2iron.fast4android.arch

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.github.keep2iron.fast4android.rx.RxLifecycleDispatcher
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/01/29 16:02
 */
abstract class AbstractDialogFragment<DB : ViewDataBinding> : DialogFragment(), RxLifecycleOwner {

  protected lateinit var contentView: View

  protected lateinit var dataBinding: DB

  protected lateinit var contextHolder: Context

  private var hasWindowLayout = false

  private val lifecycleDispatcher = RxLifecycleDispatcher(this)

  override val publishSubject: BehaviorSubject<LifecycleEvent> = lifecycleDispatcher.publishSubject

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

    val dialog = InnerDialog(context, R.style.dialog, this)
    dialog.setCanceledOnTouchOutside(true)

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

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val genDataBinding = DataBindingUtil.inflate<DB>(inflater, resId, null, false)
    contentView = if (genDataBinding != null) {
      dataBinding = genDataBinding
      dataBinding.root
    } else {
      inflater.inflate(resId, null, false)
    }
    contentView.fitsSystemWindows = false
    contentView.isClickable = true
    return contentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initVariables(contentView)
  }

  fun <V : View> findViewById(@IdRes id: Int): V {
    return contentView.findViewById(id)
  }

  override fun onStart() {
    super.onStart()
  }

  override fun onResume() {
    super.onResume()

    if (!hasWindowLayout) {
      val dialog = dialog
      val pair = setWidthAndHeight()
      if (pair.size != 2) {
        throw IllegalArgumentException("setWidthAndHeight() must return 2 integer value => [width,height]")
      }
      dialog.window?.setLayout(pair[0], pair[1])
      hasWindowLayout = true
    }
  }

  open fun onTouchOutside() {
  }

  class InnerDialog(
    context: Context,
    themeResId: Int,
    private val dialog: AbstractDialogFragment<out ViewDataBinding>
  ) : Dialog(context, themeResId) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
      if (!dialog.isCancelable) {
        return false
      }

      if (isShowing && event.action == MotionEvent.ACTION_DOWN && isTouchOutside(event)) {
        dialog.onTouchOutside()
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

  fun showAllowingStateLoss(manager: FragmentManager, tag: String) {
    val mDismissed = this::class.java.getDeclaredField("mDismissed")
    mDismissed.isAccessible = true
    val mShownByMe = this::class.java.getDeclaredField("mShownByMe")
    mShownByMe.isAccessible = true

    mDismissed.setBoolean(this, false)
    mShownByMe.setBoolean(this, true)

    val ft = manager.beginTransaction()
    ft.add(this, tag)
    ft.commitAllowingStateLoss()
  }
}
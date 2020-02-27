package io.github.keep2iron.fast4android.arch

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import java.lang.ref.WeakReference

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/01/29 16:02
 */
abstract class AbstractDialogFragment<DB : ViewDataBinding> : DialogFragment() {
  protected lateinit var dataBinding: DB

  private var hasWindowLayout = false

  @LayoutRes
  abstract fun resId(): Int

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
    val dialog = InnerDialog(requireContext(), R.style.dialog, this)
    dialog.setCanceledOnTouchOutside(isCancelable)
    dialog.setCancelable(isCancelable)
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

  abstract fun gravity(): Int
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val genDataBinding = DataBindingUtil.inflate<DB>(inflater, resId(), null, false)
    val contentView = if (genDataBinding != null) {
      dataBinding = genDataBinding
      dataBinding.root
    } else {
      inflater.inflate(resId(), null, false)
    }
    contentView.fitsSystemWindows = false
    contentView.isClickable = true
    return contentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initVariables(view)
  }

  fun <V : View> findViewById(@IdRes id: Int): V {
    return requireView().findViewById(id)
  }

  override fun onResume() {
    super.onResume()

    if (!hasWindowLayout) {
      requireDialog().window?.setLayout(width(), height())
      hasWindowLayout = true
    }
  }

  open fun onTouchOutside() {
  }

  class InnerDialog(
    context: Context,
    themeResId: Int,
    dialog: AbstractDialogFragment<out ViewDataBinding>
  ) : Dialog(context, themeResId) {

    private val dialogRef = WeakReference(dialog)

    override fun onTouchEvent(event: MotionEvent): Boolean {
      val dialog = dialogRef.get()

      val isCancelable = dialog?.isCancelable == true
      if (!isCancelable) {
        return false
      }

      if (isShowing && event.action == MotionEvent.ACTION_DOWN && isTouchOutside(event)) {
        dialog?.onTouchOutside()
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
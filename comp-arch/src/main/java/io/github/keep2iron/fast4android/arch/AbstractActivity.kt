package io.github.keep2iron.fast4android.arch

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.base.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.arch.rx.LifecycleEvent
import io.github.keep2iron.fast4android.arch.rx.RxLifecycleDispatcher
import io.reactivex.subjects.BehaviorSubject

abstract class AbstractActivity<DB : ViewDataBinding> : AppCompatActivity(), RxLifecycleOwner {

  companion object {
    private const val NO_REQUESTED_ORIENTATION_SET = -100
  }

  private val rxLifecycleDispatcher = RxLifecycleDispatcher(this)

  override val publishSubject: BehaviorSubject<LifecycleEvent> =
    rxLifecycleDispatcher.publishSubject

  private var mConvertToTranslucentCauseOrientationChanged = false
  private var mPendingRequestedOrientation = NO_REQUESTED_ORIENTATION_SET

  private var mIsInSwipeBack = false
  lateinit var dataBinding: DB

  protected open fun beforeInit() {

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    FastStatusBarHelper.setStatusBarLightMode(this)
    beforeInit()

    val createDataBinding: DB? = DataBindingUtil.setContentView(this, resId())
    if (createDataBinding != null) {
      dataBinding = createDataBinding
    }

    initVariables(savedInstanceState)
  }

  @LayoutRes
  abstract fun resId(): Int

  abstract fun initVariables(savedInstanceState: Bundle?)
}
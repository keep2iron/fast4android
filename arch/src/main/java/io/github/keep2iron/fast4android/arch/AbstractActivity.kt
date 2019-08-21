package io.github.keep2iron.fast4android.arch

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.Utils
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.github.keep2iron.fast4android.rx.RxLifecycleDispatcher
import io.github.keep2iron.fast4android.core.util.FastStatusBarHelper
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

  @SuppressLint("WrongConstant")
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    if (mConvertToTranslucentCauseOrientationChanged) {
      mConvertToTranslucentCauseOrientationChanged = false
      Utils.convertActivityFromTranslucent(this)
      if (mPendingRequestedOrientation != NO_REQUESTED_ORIENTATION_SET) {
        super.setRequestedOrientation(mPendingRequestedOrientation)
        mPendingRequestedOrientation = NO_REQUESTED_ORIENTATION_SET
      }
    }
  }
}
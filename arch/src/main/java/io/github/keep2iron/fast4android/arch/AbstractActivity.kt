package io.github.keep2iron.fast4android.arch

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import io.github.keep2iron.fast4android.Utils
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.reactivex.subjects.BehaviorSubject

abstract class AbstractActivity<DB : ViewDataBinding> : AppCompatActivity(), RxLifecycleOwner {

  private val NO_REQUESTED_ORIENTATION_SET = -100
  private var mConvertToTranslucentCauseOrientationChanged = false
  private var mPendingRequestedOrientation = NO_REQUESTED_ORIENTATION_SET

  private var mListenerRemover: SwipeBackLayout.ListenerRemover? = null
  private var mSwipeBackgroundView: SwipeBackgroundView? = null
  private var mIsInSwipeBack = false

  override val publishSubject: BehaviorSubject<LifecycleEvent> = BehaviorSubject.create()

  lateinit var dataBinding: DB

  protected open fun beforeInit() {

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    beforeInit()

    val createDataBinding: DB? = DataBindingUtil.setContentView(this, resId())
    if (createDataBinding != null) {
      dataBinding = createDataBinding
    }

    (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    publishSubject.onNext(LifecycleEvent.CREATE)

    initVariables(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    publishSubject.onNext(LifecycleEvent.START)
  }

  override fun onResume() {
    super.onResume()
    publishSubject.onNext(LifecycleEvent.RESUME)
  }

  override fun onPause() {
    super.onPause()
    publishSubject.onNext(LifecycleEvent.PAUSE)
  }

  override fun onStop() {
    super.onStop()
    publishSubject.onNext(LifecycleEvent.STOP)
  }

  override fun onDestroy() {
    publishSubject.onNext(LifecycleEvent.DESTROY)
    super.onDestroy()
  }

  @LayoutRes
  abstract fun resId(): Int

  /**
   * 在这个方法中可以进行重新 编写控件的逻辑
   */
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
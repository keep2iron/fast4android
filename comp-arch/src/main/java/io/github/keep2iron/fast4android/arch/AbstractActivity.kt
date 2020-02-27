package io.github.keep2iron.fast4android.arch

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.base.util.FastStatusBarHelper

abstract class AbstractActivity<DB : ViewDataBinding> : AppCompatActivity() {

  companion object {
    private const val NO_REQUESTED_ORIENTATION_SET = -100
  }

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
package io.github.keep2iron.fast4android.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.reactivex.subjects.BehaviorSubject

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/19 11:08
 */
abstract class AbstractFragment<DB : ViewDataBinding> : androidx.fragment.app.Fragment(), RxLifecycleOwner {
  private var isInit: Boolean = false

  private var isOnAttach: Boolean = false

  private var waitingForUser: Boolean = false

  override val publishSubject: BehaviorSubject<LifecycleEvent> =
    BehaviorSubject.create<LifecycleEvent>()

  lateinit var dataBinding: DB

  lateinit var contentView: View

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    isOnAttach = true
  }

  @LayoutRes
  abstract fun resId(): Int

  /**
   * 初始化方法
   *
   * @param container 被映射的container对象
   */
  abstract fun initVariables(container: View, savedInstanceState: Bundle?)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(resId(), container, false)
    try {
      val createBinding = DataBindingUtil.bind<DB>(view)
      if (createBinding != null) {
        dataBinding = createBinding
      }
    } catch (e: IllegalArgumentException) {
    }
    contentView = view

    contentView.isClickable = true

    return contentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initVariables(contentView, savedInstanceState)
    if (userVisibleHint && !isInit) {
      isInit = true
      lazyLoad(contentView)
    }
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

  /**
   * 延迟加载
   * 子类必须重写此方法
   *
   * @param container
   */
  open fun lazyLoad(container: View?) {
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)

    if (isVisibleToUser) {
      if (isOnAttach && !isInit) {
        lazyLoad(view)
        isInit = true
      }
    }

    if (isVisibleToUser) {
      if (parentFragment != null && !parentFragment!!.userVisibleHint) {
        waitingForUser = true
        return
      }
    }

    if (isOnAttach && host != null) {
      val fragments: List<androidx.fragment.app.Fragment>? = childFragmentManager.fragments
      fragments?.forEach {
        if (it is AbstractFragment<*>) {
          if (userVisibleHint) {
            if (it.userVisibleHint || it.waitingForUser) {
              it.userVisibleHint = true
              it.waitingForUser = false
            }
          } else {
            if (it.userVisibleHint) {
              it.userVisibleHint = false
              it.waitingForUser = true
            }
          }
        }
      }
    }
  }

  override fun onDetach() {
    isOnAttach = false
    super.onDetach()
  }

  fun <T : View> findViewById(viewId: Int): T {
    return contentView.findViewById(viewId) as T
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)

    if (isAdded) {
      val fragments: List<androidx.fragment.app.Fragment>? = childFragmentManager.fragments
      fragments?.forEach {
        if (!it.isHidden && it.userVisibleHint) {
          it.onHiddenChanged(hidden)
        }
      }
    }
  }
}
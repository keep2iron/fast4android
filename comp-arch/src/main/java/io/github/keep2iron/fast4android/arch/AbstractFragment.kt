package io.github.keep2iron.fast4android.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/19 11:08
 */
abstract class AbstractFragment<DB : ViewDataBinding> : Fragment() {

  lateinit var dataBinding: DB

  lateinit var contentView: View

  /**
   * lazy load 的标志
   */
  var loaded = false

  @LayoutRes
  abstract fun resId(): Int

  abstract fun initVariables(savedInstanceState: Bundle?)

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
    initVariables(savedInstanceState)
  }

  fun <T : View> findViewById(viewId: Int): T {
    return contentView.findViewById(viewId) as T
  }

}
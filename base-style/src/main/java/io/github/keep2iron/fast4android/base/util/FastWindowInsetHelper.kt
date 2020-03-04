/*
 * Tencent is pleased to support the open source community by making fast_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.keep2iron.fast4android.base.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import io.github.keep2iron.fast4android.base.R
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.base.widget.INotchInsetConsumer
import io.github.keep2iron.fast4android.base.widget.IWindowInsetLayout
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.max

/**
 * @author cginechen
 * @date 2017-09-13
 */
class FastWindowInsetHelper(viewGroup: ViewGroup, windowInsetLayout: IWindowInsetLayout) {
  companion object {
    // 100dp
    const val KEYBOARD_HEIGHT_BOUNDARY = 100
    val KEYBOARD_CONSUMER = Any()
    private val sCustomHandlerContainerList = ArrayList<Class<out ViewGroup>>()

    @TargetApi(19)
    fun jumpDispatch(child: View): Boolean {
      return !child.fitsSystemWindows && !isHandleContainer(
        child
      )
    }

    fun isHandleContainer(child: View): Boolean {
      val ret = child is IWindowInsetLayout ||
        child is CoordinatorLayout ||
        child is DrawerLayout
      if (ret) {
        return true
      }
      for (clz in sCustomHandlerContainerList) {
        if (clz.isInstance(child)) {
          return true
        }
      }
      return false
    }

    fun addHandleContainer(clazz: Class<out ViewGroup>) {
      sCustomHandlerContainerList.add(clazz)
    }

    fun findKeyboardAreaConsumer(@NonNull view: View?): View? {
      var varView = view
      while (varView != null) {
        val tag = varView.getTag(R.id.fast_window_inset_keyboard_area_consumer)
        if (KEYBOARD_CONSUMER === tag) {
          return varView
        }
        val viewParent = varView.parent
        if (viewParent is View) {
          varView = viewParent
        } else {
          varView = null
        }
      }
      return null
    }
  }

  private val mWindowInsetLayoutWR: WeakReference<IWindowInsetLayout> =
    WeakReference(windowInsetLayout)
  private var sApplySystemWindowInsetsCount = 0

  init {
    if (FastNotchHelper.isNotchOfficialSupport) {
      setOnApplyWindowInsetsListener28(viewGroup)
    } else {
      // some rom crash with WindowInsets...
      ViewCompat.setOnApplyWindowInsetsListener(viewGroup,
        object : OnApplyWindowInsetsListener {
          override fun onApplyWindowInsets(
            v: View,
            insets: WindowInsetsCompat
          ): WindowInsetsCompat {
            val insetLayout = mWindowInsetLayoutWR.get()
            if (Build.VERSION.SDK_INT >= 21 && insetLayout != null) {
              if (insetLayout.applySystemWindowInsets21(insets)) {
                return insets.consumeSystemWindowInsets()
              }
            }
            return insets
          }
        })
    }
  }

  @TargetApi(28)
  private fun setOnApplyWindowInsetsListener28(viewGroup: ViewGroup) {
    // WindowInsetsCompat does not exist DisplayCutout stuff...
    viewGroup.setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener { _, windowInsets ->
      var windowInsetsParams = windowInsets
      val insetLayout = mWindowInsetLayoutWR.get()
      if (insetLayout != null && insetLayout.applySystemWindowInsets21(
          windowInsetsParams
        )
      ) {
        windowInsetsParams = windowInsetsParams.consumeSystemWindowInsets()
        val displayCutout = windowInsetsParams.displayCutout
        if (displayCutout != null) {
          windowInsetsParams = windowInsetsParams.consumeDisplayCutout()
        }
        return@OnApplyWindowInsetsListener windowInsetsParams
      }
      windowInsetsParams
    })
  }

  @TargetApi(19)
  fun defaultApplySystemWindowInsets19(viewGroup: ViewGroup, insets: Rect): Boolean {
    var consumed = false
    if (insets.bottom >= dp2px(viewGroup.context, KEYBOARD_HEIGHT_BOUNDARY)) {
      viewGroup.setPaddingBottom(insets.bottom)
      viewGroup.setTag(
        R.id.fast_window_inset_keyboard_area_consumer,
        KEYBOARD_CONSUMER
      )
      insets.bottom = 0
    } else {
      viewGroup.setTag(R.id.fast_window_inset_keyboard_area_consumer, null)
      viewGroup.setPaddingBottom(0)
    }

    for (i in 0 until viewGroup.childCount) {
      val child = viewGroup.getChildAt(i)
      if (jumpDispatch(
          child
        )
      ) {
        continue
      }
      val childInsets = Rect(insets)
      computeInsetsWithGravity(child, childInsets)

      if (!isHandleContainer(
          child
        )
      ) {
        child.setPadding(childInsets.left, childInsets.top, childInsets.right, childInsets.bottom)
      } else {
        consumed = if (child is IWindowInsetLayout) {
          val output = (child as IWindowInsetLayout).applySystemWindowInsets19(childInsets)
          consumed || output
        } else {
          val output = defaultApplySystemWindowInsets19(child as ViewGroup, childInsets)
          consumed || output
        }
      }
    }

    return consumed
  }

  @TargetApi(21)
  fun defaultApplySystemWindowInsets21(viewGroup: ViewGroup, insets: Any): Boolean {
    return if (FastNotchHelper.isNotchOfficialSupport) {
      defaultApplySystemWindowInsets(viewGroup, insets as WindowInsets)
    } else {
      defaultApplySystemWindowInsetsCompat(viewGroup, insets as WindowInsetsCompat)
    }
  }

  @TargetApi(21)
  fun defaultApplySystemWindowInsetsCompat(
    viewGroup: ViewGroup,
    insets: WindowInsetsCompat
  ): Boolean {
    if (!insets.hasSystemWindowInsets()) {
      return false
    }
    var consumed = false
    var showKeyboard = false
    if (insets.systemWindowInsetBottom >= dp2px(viewGroup.context, KEYBOARD_HEIGHT_BOUNDARY)) {
      showKeyboard = true
      viewGroup.setPaddingBottom(insets.systemWindowInsetBottom)
      viewGroup.setTag(
        R.id.fast_window_inset_keyboard_area_consumer,
        KEYBOARD_CONSUMER
      )
    } else {
      viewGroup.setPaddingBottom(0)
      viewGroup.setTag(R.id.fast_window_inset_keyboard_area_consumer, null)
    }

    for (i in 0 until viewGroup.childCount) {
      val child = viewGroup.getChildAt(i)

      if (jumpDispatch(
          child
        )
      ) {
        continue
      }
      var insetLeft = insets.systemWindowInsetLeft
      var insetRight = insets.systemWindowInsetRight
      if (FastNotchHelper.needFixLandscapeNotchAreaFitSystemWindow(
          viewGroup
        ) && viewGroup.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
      ) {
        insetLeft = max(
          insetLeft,
          FastNotchHelper.getSafeInsetLeft(viewGroup)
        )
        insetRight = max(
          insetRight,
          FastNotchHelper.getSafeInsetRight(viewGroup)
        )
      }
      val childInsets = Rect(
        insetLeft,
        insets.systemWindowInsetTop,
        insetRight,
        if (showKeyboard) 0 else insets.systemWindowInsetBottom
      )

      computeInsetsWithGravity(child, childInsets)
      val windowInsetsCompat =
        ViewCompat.dispatchApplyWindowInsets(child, insets.replaceSystemWindowInsets(childInsets))
      consumed = consumed || windowInsetsCompat != null && windowInsetsCompat.isConsumed
    }

    return consumed
  }

  @TargetApi(28)
  fun defaultApplySystemWindowInsets(viewGroup: ViewGroup, insets: WindowInsets): Boolean {
    var insets = insets
    sApplySystemWindowInsetsCount++
    if (FastNotchHelper.isNotchOfficialSupport) {
      if (sApplySystemWindowInsetsCount == 1) {
        // avoid dispatching multiple times
        dispatchNotchInsetChange(viewGroup)
      }
      // always consume display cutout!!
      insets = insets.consumeDisplayCutout()
    }
    var consumed = false
    if (insets.hasSystemWindowInsets()) {
      var showKeyboard = false
      if (insets.systemWindowInsetBottom >= dp2px(viewGroup.context, KEYBOARD_HEIGHT_BOUNDARY)) {
        showKeyboard = true
        viewGroup.setPaddingBottom(insets.systemWindowInsetBottom)
        viewGroup.setTag(
          R.id.fast_window_inset_keyboard_area_consumer,
          KEYBOARD_CONSUMER
        )
      } else {
        viewGroup.setPaddingBottom(0)
        viewGroup.setTag(R.id.fast_window_inset_keyboard_area_consumer, null)
      }
      for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)

        if (jumpDispatch(
            child
          )
        ) {
          continue
        }
        val childInsets = Rect(
          insets.systemWindowInsetLeft,
          insets.systemWindowInsetTop,
          insets.systemWindowInsetRight,
          if (showKeyboard) 0 else insets.systemWindowInsetBottom
        )
        computeInsetsWithGravity(child, childInsets)
        val childWindowInsets = insets.replaceSystemWindowInsets(childInsets)
        val windowInsets = child.dispatchApplyWindowInsets(childWindowInsets)
        consumed = consumed || windowInsets.isConsumed
      }
    }
    sApplySystemWindowInsetsCount--
    return consumed
  }

  private fun dispatchNotchInsetChange(view: View) {
    if (view is INotchInsetConsumer) {
      val stop =
        (view as INotchInsetConsumer).notifyInsetMaybeChanged()
      if (stop) {
        return
      }
    }
    if (view is ViewGroup) {
      val childCount = view.childCount
      for (i in 0 until childCount) {
        dispatchNotchInsetChange(view.getChildAt(i))
      }
    }
  }

  @SuppressLint("RtlHardcoded")
  fun computeInsetsWithGravity(view: View, insets: Rect) {
    val lp = view.layoutParams
    var gravity = -1
    if (lp is FrameLayout.LayoutParams) {
      gravity = lp.gravity
    }
    /**
     * 因为该方法执行时机早于 FrameLayout.layoutChildren，
     * 而在 {FrameLayout#layoutChildren} 中当 gravity == -1 时会设置默认值为 Gravity.TOP | Gravity.LEFT，
     * 所以这里也要同样设置
     */
    if (gravity == -1) {
      gravity = Gravity.TOP or Gravity.LEFT
    }

    if (lp.width != FrameLayout.LayoutParams.MATCH_PARENT) {
      val horizontalGravity = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
      when (horizontalGravity) {
        Gravity.LEFT -> insets.right = 0
        Gravity.RIGHT -> insets.left = 0
      }
    }

    if (lp.height != FrameLayout.LayoutParams.MATCH_PARENT) {
      val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
      when (verticalGravity) {
        Gravity.TOP -> insets.bottom = 0
        Gravity.BOTTOM -> insets.top = 0
      }
    }
  }
}

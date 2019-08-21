/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
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

package io.github.keep2iron.fast4android

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityOptions
import android.os.Build
import android.os.Looper
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import io.github.keep2iron.fast4android.core.FastLogger
import java.lang.reflect.Field

/**
 * Created by Chaojun Wang on 6/9/14.
 */
object Utils {

  private var sOldBackStackEntryImpl = false

  private var sOldOpImpl = false

  /**
   * Convert a translucent themed Activity
   * [android.R.attr.windowIsTranslucent] to a fullscreen opaque
   * Activity.
   *
   *
   * Call this whenever the background of a translucent Activity has changed
   * to become opaque. Doing so will allow the [android.view.Surface] of
   * the Activity behind to be released.
   *
   *
   * This call has no effect on non-translucent activities or on activities
   * with the [android.R.attr.windowIsFloating] attribute.
   */
  fun convertActivityFromTranslucent(activity: Activity) {
    try {
      @SuppressLint("PrivateApi") val method =
        Activity::class.java.getDeclaredMethod("convertFromTranslucent")
      method.isAccessible = true
      method.invoke(activity)
    } catch (ignore: Throwable) {
    }

  }

  /**
   * Convert a translucent themed Activity
   * [android.R.attr.windowIsTranslucent] back from opaque to
   * translucent following a call to
   * [.convertActivityFromTranslucent] .
   *
   *
   * Calling this allows the Activity behind this one to be seen again. Once
   * all such Activities have been redrawn
   *
   *
   * This call has no effect on non-translucent activities or on activities
   * with the [android.R.attr.windowIsFloating] attribute.
   */
  fun convertActivityToTranslucent(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      convertActivityToTranslucentAfterL(activity)
    } else {
      convertActivityToTranslucentBeforeL(activity)
    }
  }

  /**
   * Calling the convertToTranslucent method on platforms before Android 5.0
   */
  private fun convertActivityToTranslucentBeforeL(activity: Activity) {
    try {
      val classes = Activity::class.java.declaredClasses
      var translucentConversionListenerClazz: Class<*>? = null
      for (clazz in classes) {
        if (clazz.simpleName.contains("TranslucentConversionListener")) {
          translucentConversionListenerClazz = clazz
        }
      }
      @SuppressLint("PrivateApi") val method = Activity::class.java.getDeclaredMethod(
        "convertToTranslucent",
        translucentConversionListenerClazz!!
      )
      method.isAccessible = true
      method.invoke(activity, null)
    } catch (ignore: Throwable) {
    }

  }

  /**
   * Calling the convertToTranslucent method on platforms after Android 5.0
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private fun convertActivityToTranslucentAfterL(activity: Activity) {
    try {
      @SuppressLint("PrivateApi") val getActivityOptions =
        Activity::class.java.getDeclaredMethod("getActivityOptions")
      getActivityOptions.isAccessible = true
      val options = getActivityOptions.invoke(activity)

      val classes = Activity::class.java.declaredClasses
      var translucentConversionListenerClazz: Class<*>? = null
      for (clazz in classes) {
        if (clazz.simpleName.contains("TranslucentConversionListener")) {
          translucentConversionListenerClazz = clazz
        }
      }
      @SuppressLint("PrivateApi") val convertToTranslucent = Activity::class.java.getDeclaredMethod(
        "convertToTranslucent",
        translucentConversionListenerClazz, ActivityOptions::class.java
      )
      convertToTranslucent.isAccessible = true
      convertToTranslucent.invoke(activity, null, options)
    } catch (ignore: Throwable) {
    }

  }

  fun assertInMainThread() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      val elements = Thread.currentThread().stackTrace
      var methodMsg: String? = null
      if (elements != null && elements.size >= 4) {
        methodMsg = elements[3].toString()
      }
      throw IllegalStateException("Call the method must be in main thread: " + methodMsg!!)
    }
  }

  internal fun findAndModifyOpInBackStackRecord(
    fragmentManager: FragmentManager?,
    backStackIndex: Int,
    handler: OpHandler?
  ) {
    var backStackIndexVar = backStackIndex
    if (fragmentManager == null || handler == null) {
      return
    }
    val backStackCount = fragmentManager.backStackEntryCount
    if (backStackCount > 0) {
      if (backStackIndexVar >= backStackCount || backStackIndexVar < -backStackCount) {
        FastLogger.d(
          "findAndModifyOpInBackStackRecord", "backStackIndex error: " +
              "backStackIndex = " + backStackIndexVar + " ; backStackCount = " + backStackCount
        )
        return
      }
      if (backStackIndexVar < 0) {
        backStackIndexVar += backStackCount
      }
      try {
        val backStackEntry = fragmentManager.getBackStackEntryAt(backStackIndexVar)

        if (handler.needReNameTag()) {
          val nameField = getNameField(backStackEntry)
          if (nameField != null) {
            nameField.isAccessible = true
            nameField.set(backStackEntry, handler.newTagName())
          }
        }

        val opsField = Utils.getOpsField(backStackEntry)
        opsField!!.isAccessible = true
        val opsObj = opsField.get(backStackEntry)
        if (opsObj is List<*>) {
          for (op in opsObj) {
            if (handler.handle(op)) {
              return
            }
          }
        }
      } catch (e: IllegalAccessException) {
        e.printStackTrace()
      }

    }
  }

  internal fun getBackStackEntryField(
    backStackEntry: FragmentManager.BackStackEntry,
    name: String
  ): Field? {
    var opsField: Field? = null
    if (!sOldBackStackEntryImpl) {
      try {
        opsField = FragmentTransaction::class.java.getDeclaredField(name)
      } catch (ignore: NoSuchFieldException) {
      }

    }

    if (opsField == null) {
      sOldBackStackEntryImpl = true
      try {
        opsField = backStackEntry.javaClass.getDeclaredField(name)
      } catch (ignore: NoSuchFieldException) {
      }

    }
    return opsField
  }

  internal fun getOpsField(backStackEntry: FragmentManager.BackStackEntry): Field? {
    return getBackStackEntryField(backStackEntry, "mOps")
  }

  internal fun getNameField(backStackEntry: FragmentManager.BackStackEntry): Field? {
    return getBackStackEntryField(backStackEntry, "mName")
  }

  private fun getOpField(op: Any, fieldNameNew: String, fieldNameOld: String): Field? {
    var field: Field? = null
    if (!sOldOpImpl) {
      try {
        field = op.javaClass.getDeclaredField(fieldNameNew)
      } catch (ignore: NoSuchFieldException) {

      }

    }

    if (field == null) {
      sOldOpImpl = true
      try {
        field = op.javaClass.getDeclaredField(fieldNameOld)
      } catch (ignore: NoSuchFieldException) {
      }

    }
    return field
  }

  internal fun getOpCmdField(op: Any): Field? {
    return getOpField(op, "mCmd", "cmd")
  }

  internal fun getOpFragmentField(op: Any): Field? {
    return getOpField(op, "mFragment", "fragment")
  }

  internal fun getOpPopEnterAnimField(op: Any): Field? {
    return getOpField(op, "mPopEnterAnim", "popEnterAnim")
  }

  internal fun getOpPopExitAnimField(op: Any): Field? {
    return getOpField(op, "mPopExitAnim", "popExitAnim")
  }

  internal interface OpHandler {
    fun handle(op: Any?): Boolean

    fun needReNameTag(): Boolean

    fun newTagName(): String
  }
}

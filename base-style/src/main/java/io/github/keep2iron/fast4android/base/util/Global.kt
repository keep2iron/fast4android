package io.github.keep2iron.fast4android.base.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import androidx.collection.ArrayMap
import io.github.keep2iron.fast4android.base.ComponentProvider
import io.github.keep2iron.fast4android.base.Fast4Android
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

/**
 * 用于扩展 Application 的扩展方法类
 */
val COMPONENT_SERVICE = ArrayMap<Class<*>, Any>()

/**
 * 注册组件服务
 */
inline fun <reified T> registerComponent(componentProvider: ComponentProvider<T>) {
  COMPONENT_SERVICE[T::class.java] =
    componentProvider.onCreateComponent(Fast4Android.CONTEXT.applicationContext as Application)
}

/**
 * 获取组件
 */
inline fun <reified T> getComponentService(): T {
  return COMPONENT_SERVICE[T::class.java] as T
}


inline fun <reified T : Activity> push(vararg args: Pair<String, Any>) {
  val intent = Intent(Fast4Android.CONTEXT, T::class.java)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  for (arg in args) {
    when (val value = arg.second) {
      is String -> {
        intent.putExtra(arg.first, value)
      }
      is Int -> {
        intent.putExtra(arg.first, value)
      }
      is Double -> {
        intent.putExtra(arg.first, value)
      }
      is Float -> {
        intent.putExtra(arg.first, value)
      }
      is Byte -> {
        intent.putExtra(arg.first, value)
      }
      is Boolean -> {
        intent.putExtra(arg.first, value)
      }
      is Bundle -> {
        intent.putExtra(arg.first, value)
      }
      is Long -> {
        intent.putExtra(arg.first, value)
      }
      is Char -> {
        intent.putExtra(arg.first, value)
      }
      is Short -> {
        intent.putExtra(arg.first, value)
      }
      is Parcelable -> {
        intent.putExtra(arg.first, value)
      }
      is IntArray -> {
        intent.putExtra(arg.first, value)
      }
      is ByteArray -> {
        intent.putExtra(arg.first, value)
      }
      is FloatArray -> {
        intent.putExtra(arg.first, value)
      }
      is DoubleArray -> {
        intent.putExtra(arg.first, value)
      }
      is BooleanArray -> {
        intent.putExtra(arg.first, value)
      }
      is Serializable -> {
        intent.putExtra(arg.first, value)
      }
      is LongArray -> {
        intent.putExtra(arg.first, value)
      }
      is CharSequence -> {
        intent.putExtra(arg.first, value)
      }
    }
  }
  Fast4Android.CONTEXT.startActivity(intent)
}

/**
 * 计算当前颜色值与目标颜色值的中间值
 *
 * @param toColor 目标颜色值
 * @param fractionParams [0,1] 过渡的百分比
 */
fun Int.computeColor(toColor: Int, fractionParams: Float): Int {
  val fromColor = this
  val fraction = max(min(fractionParams, 1f), 0f)

  val minColorA = Color.alpha(fromColor)
  val maxColorA = Color.alpha(toColor)
  val resultA = ((maxColorA - minColorA) * fraction).toInt() + minColorA

  val minColorR = Color.red(fromColor)
  val maxColorR = Color.red(toColor)
  val resultR = ((maxColorR - minColorR) * fraction).toInt() + minColorR

  val minColorG = Color.green(fromColor)
  val maxColorG = Color.green(toColor)
  val resultG = ((maxColorG - minColorG) * fraction).toInt() + minColorG

  val minColorB = Color.blue(fromColor)
  val maxColorB = Color.blue(toColor)
  val resultB = ((maxColorB - minColorB) * fraction).toInt() + minColorB

  return Color.argb(resultA, resultR, resultG, resultB)
}
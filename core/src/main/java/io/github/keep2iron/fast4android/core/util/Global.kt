package io.github.keep2iron.fast4android.core.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import io.github.keep2iron.fast4android.core.Fast4Android
import java.io.Serializable

fun dp2px(dp: Int): Int {
  val density = Fast4Android.CONTEXT.resources.displayMetrics.density
  return (dp * density).toInt()
}

fun px2dp(px: Int): Int {
  val density = Fast4Android.CONTEXT.resources.displayMetrics.density
  return (px / density).toInt()
}

fun sp(sp: Int): Float {
  val scaledDensity = Fast4Android.CONTEXT.resources.displayMetrics.scaledDensity
  return sp * scaledDensity
}

fun startActivity(clazz: Class<out Activity>, vararg args: Pair<String, Any>) {
  val intent = Intent(Fast4Android.CONTEXT, clazz)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  for (arg in args) {
    val value = arg.second
    when (value) {
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
package io.github.keep2iron.fast4android.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import io.github.keep2iron.fast4android.Fast4Android

fun layout(@LayoutRes layoutId: Int, parent: ViewGroup? = null): View {
  return LayoutInflater.from(Fast4Android.CONTEXT).inflate(layoutId, parent)
}

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
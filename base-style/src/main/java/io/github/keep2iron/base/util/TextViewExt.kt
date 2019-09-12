package io.github.keep2iron.base.util

import android.content.Context
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun TextView.setComponentLeftDrawable(
  context: Context,
  @DrawableRes drawableIdRes: Int,
  width: Int,
  height: Int
) {
  val drawable = ContextCompat.getDrawable(context, drawableIdRes)
  drawable?.setBounds(0, 0, width, height)
  this.setCompoundDrawables(
    drawable,
    compoundDrawables[1],
    compoundDrawables[2],
    compoundDrawables[3]
  )
}

fun TextView.setComponentTopDrawable(
  context: Context,
  @DrawableRes drawableIdRes: Int,
  width: Int,
  height: Int
) {
  val drawable = ContextCompat.getDrawable(context, drawableIdRes)
  drawable?.setBounds(0, 0, width, height)
  this.setCompoundDrawables(
    compoundDrawables[0],
    drawable,
    compoundDrawables[2],
    compoundDrawables[3]
  )
}

fun TextView.setComponentRightDrawable(
  context: Context,
  @DrawableRes drawableIdRes: Int,
  width: Int,
  height: Int
) {
  val drawable = ContextCompat.getDrawable(context, drawableIdRes)
  drawable?.setBounds(0, 0, width, height)
  this.setCompoundDrawables(
    compoundDrawables[0],
    compoundDrawables[1],
    drawable,
    compoundDrawables[3]
  )
}

fun TextView.setComponentBottomDrawable(
  context: Context,
  @DrawableRes drawableIdRes: Int,
  width: Int,
  height: Int
) {
  val drawable = ContextCompat.getDrawable(context, drawableIdRes)
  drawable?.setBounds(0, 0, width, height)
  this.setCompoundDrawables(
    compoundDrawables[0],
    compoundDrawables[1],
    compoundDrawables[2],
    drawable
  )
}
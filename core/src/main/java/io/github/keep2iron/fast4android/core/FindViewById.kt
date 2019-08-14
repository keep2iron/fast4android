package io.github.keep2iron.fast4android.core

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import android.view.View
import kotlin.reflect.KProperty

class FindViewById(@IdRes private val id: Int) {

  operator fun <T : View> getValue(thisRef: Activity, property: KProperty<*>): T {
    return thisRef.findViewById(id)
  }

  operator fun setValue(thisRef: Activity, property: KProperty<*>, view: View) {
    throw IllegalArgumentException("did't be allow to call setValue")
  }

  operator fun <T : View> getValue(thisRef: androidx.fragment.app.Fragment, property: KProperty<*>): T {
    return thisRef.view?.findViewById(id)!!
  }

  operator fun setValue(thisRef: androidx.fragment.app.Fragment, property: KProperty<*>, view: View) {
    throw IllegalArgumentException("did't be allow to call setValue")
  }
}

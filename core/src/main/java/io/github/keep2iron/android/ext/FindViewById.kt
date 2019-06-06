package io.github.keep2iron.android.ext

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.View
import java.lang.IllegalArgumentException
import kotlin.reflect.KProperty

class FindViewById(@IdRes private val id: Int) {

    operator fun <T : View> getValue(thisRef: Activity, property: KProperty<*>): T {
        return thisRef.findViewById(id)
    }

    operator fun setValue(thisRef: Activity, property: KProperty<*>, view: View) {
        throw IllegalArgumentException("did't be allow to call setValue")
    }

    operator fun <T : View> getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.view?.findViewById(id)!!
    }

    operator fun setValue(thisRef: Fragment, property: KProperty<*>, view: View) {
        throw IllegalArgumentException("did't be allow to call setValue")
    }
}

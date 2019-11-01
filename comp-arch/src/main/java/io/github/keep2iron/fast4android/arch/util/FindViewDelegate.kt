package io.github.keep2iron.fast4android.arch.util

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class ActivityFindViewById<T : View>(@IdRes private val id: Int) : ReadWriteProperty<Activity, T> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return thisRef.findViewById(id)
    }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        throw IllegalArgumentException("did't be allow to call setValue")
    }
}

private class FragmentFindViewById<T : View>(@IdRes private val id: Int) : ReadWriteProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.view?.findViewById(id)!!
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        throw IllegalArgumentException("did't be allow to call setValue")
    }

}

fun <T : View> Activity.findViewByDelegate(@IdRes id: Int): ReadWriteProperty<Activity, T> {
    return ActivityFindViewById(id)
}

fun <T : View> Fragment.findViewByDelegate(@IdRes id: Int): ReadWriteProperty<Fragment, T> {
    return FragmentFindViewById(id)
}
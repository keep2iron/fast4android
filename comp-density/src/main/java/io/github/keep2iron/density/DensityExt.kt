package io.github.keep2iron.density

import android.app.Application
import android.content.res.Resources
import java.lang.reflect.InvocationTargetException

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */

fun getStatusBarHeight(): Int {
    var result = 0
    try {
        val resourceId = Resources.getSystem()
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId)
        }
    } catch (e:NullPointerException) {
        e.printStackTrace()
    }
    return result
}

fun findClassByClassName(className: String): Boolean {
    val hasDependency: Boolean = try {
        Class.forName(className)
        true
    } catch (e: ClassNotFoundException) {
        false
    }
    return hasDependency
}

fun getApplicationByReflect(): Application {
    try {
        val activityThread = Class.forName("android.app.ActivityThread")
        val thread = activityThread.getMethod("currentActivityThread").invoke(null)
        val app = activityThread.getMethod("getApplication").invoke(thread)
            ?: throw NullPointerException("you should init first")
        return app as Application
    } catch (e: NoSuchMethodException) {
        e.printStackTrace();
    } catch (e: IllegalAccessException) {
        e.printStackTrace();
    } catch (e: InvocationTargetException) {
        e.printStackTrace();
    } catch (e: ClassNotFoundException) {
        e.printStackTrace();
    }
    throw java.lang.NullPointerException("you should init first")
}
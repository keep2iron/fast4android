package io.github.keep2iron.app

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import io.github.keep2iron.android.ComponentModuleProvider

class ScreenDensityModule : ComponentModuleProvider {

    companion object {
        @JvmField
        var sNonCompatDensity: Float = 0f
        @JvmField
        var sNonCompatScaleDensity: Float = 0f
    }


    override fun createComponentModule(application: Application) {
        val fragmentLifeCycleListener = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(fm: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
                val activity = fragment.activity
                if (activity != null) {
                    setCustomDensity(activity, activity.application)
                }
            }
        }

        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                setCustomDensity(activity, activity.application)
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifeCycleListener, true)
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifeCycleListener)
                }
            }
        })
    }

    fun setCustomDensity(activity: Activity, application: Application) {
        val applicationDisplayMetrics = application.resources.displayMetrics
        if (sNonCompatDensity == 0f) {
            sNonCompatDensity = applicationDisplayMetrics.density
            sNonCompatScaleDensity = applicationDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks2 {
                override fun onTrimMemory(level: Int) {

                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }

        val targetDensity = applicationDisplayMetrics.widthPixels / 375f
        val targetScaledDensity = sNonCompatScaleDensity * (targetDensity / sNonCompatDensity)
        val targetDpi = (targetDensity * 160).toInt()

        val bitmapClazz = Class.forName("android.graphics.Bitmap")
        val method = bitmapClazz.getMethod("setDefaultDensity", Int::class.java)
        method.invoke(null, targetDpi)

        applicationDisplayMetrics.density = targetDensity
        applicationDisplayMetrics.densityDpi = targetDpi
        applicationDisplayMetrics.scaledDensity = targetScaledDensity

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDpi
    }
}
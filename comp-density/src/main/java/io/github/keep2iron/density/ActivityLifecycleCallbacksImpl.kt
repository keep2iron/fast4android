package io.github.keep2iron.density

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
class ActivityLifecycleCallbacksImpl(private var densityAdaptStrategy: DensityAdaptStrategy):Application.ActivityLifecycleCallbacks{
    private val mFragmentLifecycleCallbacksToAndroidx:FragmentLifecycleCallbacksImplToAndroidx = FragmentLifecycleCallbacksImplToAndroidx(densityAdaptStrategy)
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if(activity is FragmentActivity){
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacksToAndroidx ,true)
        }
        densityAdaptStrategy.applyAdapt(activity, activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
    }

}
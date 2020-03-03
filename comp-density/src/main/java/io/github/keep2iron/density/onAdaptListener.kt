package io.github.keep2iron.density

import android.app.Activity

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
interface onAdaptListener {
    /**
     * 在屏幕适配前调用
     *
     * @param target   需要屏幕适配的对象 (可能是 {@link Activity} 或者 Fragment)
     * @param activity 当前 {@link Activity}
     */
    fun onAdapteBefor(target:Any ,activity: Activity)
    /**
     * 在屏幕适配后调用
     *
     * @param target   需要屏幕适配的对象 (可能是 {@link Activity} 或者 Fragment)
     * @param activity 当前 {@link Activity}
     */
    fun onAdapteAfter(target:Any ,activity: Activity)
}
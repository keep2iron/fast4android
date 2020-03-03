package io.github.keep2iron.density

import android.app.Activity

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
interface DensityAdaptStrategy {
    /**
     * 开始执行屏幕适配逻辑
     *
     * @param target   需要屏幕适配的对象 (可能是 {@link Activity} 或者 Fragment)
     * @param activity 需要拿到当前的 {@link Activity} 才能修改 {@link DisplayMetrics#density}
     */
    fun applyAdapt(target:Any, activity: Activity)
}
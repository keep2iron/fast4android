package com.chaomeng.density

import android.app.Activity
import io.github.keep2iron.density.DensityAdaptStrategy
import io.github.keep2iron.density.DensityConfig

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
class WrapperAutoAdaptStrategy(val densityAdaptStrategy: DensityAdaptStrategy) : DensityAdaptStrategy {
    override fun applyAdapt(target: Any, activity: Activity) {
        val mOnAdaptListener = DensityConfig.getOnAdaptListener()
        mOnAdaptListener?.onAdapteBefor(target , activity)
        densityAdaptStrategy.applyAdapt(target , activity)
        mOnAdaptListener?.onAdapteAfter(target , activity)
    }
}
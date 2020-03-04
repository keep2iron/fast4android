package io.github.keep2iron.density

import android.app.Activity
import android.util.Log

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
class DefaultDensityAdaptStrategy : DensityAdaptStrategy {

  override fun applyAdapt(target: Any, activity: Activity) {
    var targetDensity = 0f
    var targetDensityDpi = 0
    var targetScaledDensity = 0f
    var targetScreenWidthDp: Int
    var targetScreenHeightDp: Int

    targetDensity = if (DensityConfig.isBaseOnWidth) {
      DensityConfig.mScreenWidth * 1.0f / DensityConfig.mDesignWidthInDp
    } else {
      DensityConfig.mScreenHeight * 1.0f / DensityConfig.mDesignHeightInDp
    }

    targetScaledDensity = if (DensityConfig.privateFontScale > 0) {
      targetDensity * DensityConfig.privateFontScale
    } else {
      val systemFontScale: Float =
        if (DensityConfig.isExcludeFontScale) 1f else DensityConfig.mInitScaledDensity * 1.0f / DensityConfig.mInitDensity
      targetDensity * systemFontScale
    }
    targetDensityDpi = (targetDensity * 160).toInt()
    targetScreenWidthDp = (DensityConfig.mScreenWidth / targetDensity).toInt()
    targetScreenHeightDp = (DensityConfig.mScreenHeight / targetDensity).toInt()

    val activityDisplayMetrics = activity.resources.displayMetrics
    activityDisplayMetrics.density = targetDensity
    activityDisplayMetrics.densityDpi = targetDensityDpi
    activityDisplayMetrics.scaledDensity = targetScaledDensity
    Log.d("DensityConfigInit", "mInitDensity = ${DensityConfig.mInitDensity} | mInitDensityDpi = ${DensityConfig.mInitDensityDpi} | mInitScaledDensity = ${DensityConfig.mInitScaledDensity}} | mInitScreenWidthDp = ${DensityConfig.mInitScreenWidthDp} | mDesignHeightInDp = ${DensityConfig.mDesignHeightInDp}")
    Log.d("DensityConfigTarget", "targetDensity = ${targetDensity} | targetDensityDpi = ${targetDensityDpi} | targetScaledDensity = ${targetScaledDensity}} | targetScreenWidthDp = ${targetScreenWidthDp} | targetScreenHeightDp = ${targetScreenHeightDp}")

    if (target is CancelAdapt) {
      //取消适配
    }
    if (target is CustomAdapt) {
      //自定义适配方案
    }

  }
}
package io.github.keep2iron.density

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.widget.Toast
import com.chaomeng.density.WrapperAutoAdaptStrategy
import java.lang.reflect.Field

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
object DensityConfig {
    val KEY_DESIGN_WIDTH_IN_DP = "design_width_in_dp"
    val KEY_DESIGN_HEIGHT_IN_DP = "design_height_in_dp"

    lateinit var mApplication: Application

    /**
     * 最初的 [DisplayMetrics.density]
     */
    var mInitDensity = -1f

    /**
     * 最初的 [DisplayMetrics.densityDpi]
     */
    var mInitDensityDpi = 0

    /**
     * 最初的 [DisplayMetrics.scaledDensity]
     */
    var mInitScaledDensity = 0f

    /**
     * 最初的 [DisplayMetrics.xdpi]
     */
    var mInitXdpi = 0f
    /**
     * 最初的 [Configuration.screenWidthDp]
     */
    var mInitScreenWidthDp = 0

    /**
     * 最初的 [Configuration.screenHeightDp]
     */
    var mInitScreenHeightDp = 0

    /**
     * 设计图上的总宽度, 单位 dp
     */
    var mDesignWidthInDp = 0
    /**
     * 设计图上的总高度, 单位 dp
     */
    var mDesignHeightInDp = 0
    /**
     * 设备的屏幕总宽度, 单位 px
     */
    var mScreenWidth = 0
    /**
     * 设备的屏幕总高度, 单位 px, 如果 [.isUseDeviceSize] 为 `false`, 屏幕总高度会减去状态栏的高度
     */
    var mScreenHeight = 0
    /**
     * 状态栏高度, 当 [.isUseDeviceSize] 为 `false` 时, AndroidAutoSize 会将 [.mScreenHeight] 减去状态栏高度
     * AndroidAutoSize 默认使用 [ScreenUtils.getStatusBarHeight] 方法获取状态栏高度
     * AndroidAutoSize 使用者可使用 [.setStatusBarHeight] 自行设置状态栏高度
     */
    var mStatusBarHeight = 0
    /**
     * 为了保证在不同高宽比的屏幕上显示效果也能完全一致, 所以本方案适配时是以设计图宽度与设备实际宽度的比例或设计图高度与设备实际高度的比例应用到
     * 每个 View 上 (只能在宽度和高度之中选一个作为基准), 从而使每个 View 的高和宽用同样的比例缩放, 避免在与设计图高宽比不一致的设备上出现适配的 View 高或宽变形的问题
     * [.isBaseOnWidth] 为 `true` 时代表以宽度等比例缩放, `false` 代表以高度等比例缩放
     * [.isBaseOnWidth] 为全局配置, 默认为 `true`, 每个 [Activity] 也可以单独选择使用高或者宽做等比例缩放
     */
    var isBaseOnWidth = true
    /**
     * 此字段表示是否使用设备的实际尺寸做适配
     * [.isUseDeviceSize] 为 `true` 表示屏幕高度 [.mScreenHeight] 包含状态栏的高度
     * [.isUseDeviceSize] 为 `false` 表示 [.mScreenHeight] 会减去状态栏的高度, 默认为 `true`
     */
//    val isUseDeviceSize = true

    lateinit var mActivityLifecycleCallbacks:ActivityLifecycleCallbacksImpl

    fun getApplication(): Application {
        return mApplication
    }

    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 `true`, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 `false`, 则会跟随系统设置中字体大小的改变, 默认为 `false`
     */
    var isExcludeFontScale:Boolean = true
    fun setIsExcludeFontScale(excludeFontScale:Boolean): DensityConfig {
        isExcludeFontScale = excludeFontScale
        return this
    }

    /**
     * 区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
     * 当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 将此值设为 0 则取消此功能
     */
    var privateFontScale = 0f
    fun setPrivateFontScale(fontScale: Float): DensityConfig {
        privateFontScale = fontScale
        return this
    }

    /**
     * 是否是 Miui 系统
     */
    private var isMiui = false


    /**
     * Miui 系统中的 mTmpMetrics 字段
     */
    var mTmpMetricsField: Field? = null


    var mOnAdaptListener: onAdaptListener? = null
    fun getOnAdaptListener(): onAdaptListener? {
        return mOnAdaptListener
    }

    fun init(application: Application, isBaseOnWidth: Boolean, strategy: DensityAdaptStrategy): DensityConfig {
        check(mInitDensity == -1f) {
            Toast.makeText(
                application,
                "AutoSizeConfig#init() can only be called once",
                Toast.LENGTH_SHORT
            ).show()
        }
        mApplication = application
        DensityConfig.isBaseOnWidth = isBaseOnWidth
        val displayMetrics = Resources.getSystem().displayMetrics
        val configuration = Resources.getSystem().configuration
        getMetaData(application)

        mScreenWidth = application.resources.displayMetrics.widthPixels
        mScreenHeight = application.resources.displayMetrics.heightPixels

        mStatusBarHeight = getStatusBarHeight()

        mInitDensity = displayMetrics.density
        mInitDensityDpi = displayMetrics.densityDpi
        mInitScaledDensity = displayMetrics.scaledDensity
        mInitXdpi = displayMetrics.xdpi
        mInitScreenWidthDp = configuration.screenWidthDp
        mInitScreenHeightDp = configuration.screenHeightDp
        application.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onLowMemory() {
            }

            override fun onConfigurationChanged(newConfig: Configuration) {
                if (newConfig != null) {
                    if (newConfig.fontScale > 0) {
                        mInitScaledDensity =
                            Resources.getSystem().displayMetrics
                                .scaledDensity
                    }
                    mScreenWidth =  application.resources.displayMetrics.widthPixels
                    mScreenHeight = application.resources.displayMetrics.heightPixels
                }

            }

        })

        mActivityLifecycleCallbacks = ActivityLifecycleCallbacksImpl(WrapperAutoAdaptStrategy(densityAdaptStrategy = strategy))
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        if ("MiuiResources" == application.resources.javaClass.simpleName || "XResources" == application.resources.javaClass.simpleName) {
            isMiui = true
            try {
                mTmpMetricsField =
                    Resources::class.java.getDeclaredField("mTmpMetrics")
                mTmpMetricsField?.isAccessible = true
            } catch (e: Exception) {
                mTmpMetricsField = null
            }
        }

        return this
    }

    private fun getMetaData(context: Context) {
        kotlin.run {
            val packageManager = context.packageManager
            val applicationInfo =
                packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            if (applicationInfo.metaData != null) {
                if (applicationInfo.metaData.containsKey(KEY_DESIGN_WIDTH_IN_DP)) {
                    mDesignWidthInDp = applicationInfo.metaData.get(KEY_DESIGN_WIDTH_IN_DP) as Int
                }
                if (applicationInfo.metaData.containsKey(KEY_DESIGN_HEIGHT_IN_DP)) {
                    mDesignHeightInDp = applicationInfo.metaData.get(KEY_DESIGN_HEIGHT_IN_DP) as Int
                }
            }
        }
    }



}
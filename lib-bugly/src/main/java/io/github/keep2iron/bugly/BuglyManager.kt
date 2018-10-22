package io.github.keep2iron.bugly

import android.content.Context
import android.os.Environment
import android.support.annotation.DrawableRes
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport
import io.github.keep2iron.android.bugly.BuildConfig

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/22
 *
 * 用于代码崩溃的日志上报处理
 */
class BuglyManager private constructor() {

    var onUpgradeListener: (() -> Unit)? = null
        set(value) {
            field = value

            //如果升级回调在之前进行 则直接调用
            if (isUpgrade) {
                value?.invoke()
            }
        }

    /**
     * 是否可以升级
     */
    private var isUpgrade: Boolean = false

    init {
        /*
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        Beta.autoInit = true

        /*
          true表示初始化时自动检查升级
          false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true

        /*
         */
        Beta.initDelay = 1000

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.upgradeCheckPeriod = 60 * 1000

        /*
          设置sd卡的Download为更新资源保存目录;
          后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        /*
          点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false

        Beta.enableNotification = true

        Beta.autoDownloadOnWifi = false

        /*
          只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
          不设置会默认所有activity都可以显示弹窗;
         */
        /* 设置更新状态回调接口 */
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onUpgradeFailed(b: Boolean) {
//                ToastUtil.S(Gravity.CENTER, 0, 0, "更新失败")
            }

            override fun onUpgradeSuccess(b: Boolean) {

            }

            override fun onUpgradeNoVersion(b: Boolean) {

            }

            override fun onUpgrading(b: Boolean) {

            }

            override fun onDownloadCompleted(b: Boolean) {
//                ToastUtil.S(Gravity.CENTER, 0, 0, "下载完成")
            }
        }

        /*在application中初始化时设置监听，监听策略的收取*/
        Beta.upgradeListener = UpgradeListener { _, strategy, _, _ ->
            strategy?.apply {
                if (this.versionCode > BuildConfig.VERSION_CODE) {
                    isUpgrade = true
                    onUpgradeListener?.invoke()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        private val instance: BuglyManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BuglyManager()
        }

        @JvmStatic
        fun getBuglyManager(): BuglyManager {
            return instance
        }
    }

    /**
     * 默认初始化流程
     */
    fun init(context: Context,
             appId: String,
             @DrawableRes launcherIcon: Int,
             @DrawableRes lagerIconId: Int,
             @DrawableRes smallIconId: Int) {

        /*
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = lagerIconId

        /*
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = smallIconId

        /*
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = launcherIcon

        Bugly.init(context, appId, BuildConfig.DEBUG)
        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG)
    }
}
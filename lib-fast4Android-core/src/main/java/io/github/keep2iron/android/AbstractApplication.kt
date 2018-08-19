package io.github.keep2iron.android

import android.app.Activity
import android.app.Application
import android.support.v4.util.SparseArrayCompat
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

import java.util.ArrayList

import io.github.keep2iron.android.util.ToastUtil

/**
 * @author keep2iron
 */
abstract class AbstractApplication : Application() {
    private val mTags = SparseArrayCompat<Any>()
    private val mInStackActivity = ArrayList<Activity>()

    override fun onCreate() {
        super.onCreate()
        instance = this
        initUtils()

        initRouter()

        initRegisterComponent()
    }



    fun remove(activity: Activity) {
        mInStackActivity.remove(activity)
    }

    fun add(activity: Activity) {
        mInStackActivity.add(activity)
    }

    private fun initUtils() {
        ToastUtil.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    private fun initRouter() {
//        if (BuildConfig.DEBUG) {
//            // 打印日志
//            ARouter.openLog()
//            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//            ARouter.openDebug()
//        }
//        // 尽可能早，推荐在Application中初始化
//        ARouter.init(this)
    }

    fun setTag(key: Int, value: Any) {
        mTags.put(key, value)
    }

    fun getTag(key: Int): Any? {
        return mTags.get(key)
    }

    /**
     * 初始化注册组件，
     */
    abstract fun initRegisterComponent()

    fun closeAll() {
        for (i in mInStackActivity.indices) {
            mInStackActivity[i].finish()
        }
    }

    companion object {
        @get:JvmStatic
        lateinit var instance: AbstractApplication
    }
}
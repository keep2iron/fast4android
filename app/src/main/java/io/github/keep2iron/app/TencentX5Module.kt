package io.github.keep2iron.app

import android.app.Application
import android.util.Log
import io.github.keep2iron.android.ComponentModuleProvider
import com.tencent.smtt.sdk.QbSdk



class TencentX5Module : ComponentModuleProvider{
    override fun createComponentModule(application: Application) {
        val cb = object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(application, cb)
    }
}
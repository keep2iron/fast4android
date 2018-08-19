package io.github.keep2iron.network.interceptor

import android.util.Log

import com.orhanobut.logger.Logger
import io.github.keep2iron.network.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/10/31 15:20
 *
 * 用于打印HTTP LOG信息
 */
class HttpLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        if (!BuildConfig.DEBUG) {
            return
        }

        if (message.startsWith("{")) {
            Logger.json(message)
        } else {
            Log.e("keep2iron", message)
        }
    }
}

/*
 * Create bt Keep2iron on 17-5-25 下午2:33
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.net.interceptor

import java.io.IOException
import java.util.HashSet

import io.github.keep2iron.android.util.SPUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * This Interceptor add all received Cookies to the app DefaultPreferences.
 * Your implementation on how to save the Cookies on the Pref MAY VARY.
 *
 *
 *
 *
 * 用于保存cookie信息的拦截器
 *
 * @author tsuharesu
 * @date 4/1/15
 */
class ReceivedCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = HashSet<String>()

            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            SPUtils.instance.put("cookies", cookies)
        }

        return originalResponse
    }
}
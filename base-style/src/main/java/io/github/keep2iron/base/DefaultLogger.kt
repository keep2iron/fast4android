package io.github.keep2iron.base

import android.util.Log
import io.github.keep2iron.base.FastLogger.FastLogDelegate

class DefaultLogger : FastLogDelegate {
    override fun e(tag: String, msg: String, vararg obj: Any) {
        Log.d(tag, msg)
    }

    override fun w(tag: String, msg: String, vararg obj: Any) {
        Log.w(tag, msg)
    }

    override fun i(tag: String, msg: String, vararg obj: Any) {
        Log.i(tag, msg)
    }

    override fun d(tag: String, msg: String, vararg obj: Any) {
        Log.d(tag, msg)
    }

    override fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any) {
        Log.e(tag, Log.getStackTraceString(tr))
    }
}
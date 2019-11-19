package io.github.keep2iron.fast4android.core

import android.annotation.SuppressLint
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import io.github.keep2iron.base.Fast4Android

object Toaster {

    private var preToast: Toast? = null

    fun s(
            message: String,
            block: (ToastBuilder.() -> Unit)? = null
    ) {
        var looper = Looper.myLooper()
        if (looper == null) {
            Looper.prepare()
            looper = Looper.myLooper()
        }

        preToast?.cancel()
        val newToastBuilder = if (block != null)
            ToastBuilder().apply(block)
        else ToastBuilder()
        val toast = newToastBuilder.build(message, Toast.LENGTH_SHORT)
        preToast = toast
        toast.show()

        if (looper != null) {
            Looper.loop()
            looper.quit()
        }
    }

    fun l(
            message: String,
            block: (ToastBuilder.() -> Unit)? = null
    ) {
        preToast?.cancel()
        val newToastBuilder = if (block != null)
            ToastBuilder().apply(block)
        else ToastBuilder()
        val toast = newToastBuilder.build(message, Toast.LENGTH_LONG)
        preToast = toast
        toast.show()
    }

    class ToastBuilder constructor(
            private var gravity: Int = -1,
            private var dx: Int = 0,
            private var dy: Int = 0,
            private var customView: View? = null,
            @IdRes private var contentId: Int = 0
    ) {

        @SuppressLint("ShowToast")
        fun build(message: String, duration: Int): Toast {
            val toast = if (customView != null) {
                val tempToast = Toast(Fast4Android.CONTEXT)
                tempToast.view = customView
                customView!!.findViewById<TextView>(contentId).text = message
                tempToast
            } else {
                Toast.makeText(Fast4Android.CONTEXT, message, duration)
            }
            if (gravity != -1) {
                toast.setGravity(gravity, dx, dy)
            }
            return toast
        }
    }
}
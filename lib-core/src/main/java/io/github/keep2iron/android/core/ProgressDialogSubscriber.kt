package io.github.keep2iron.android.core

import android.app.Activity
import android.app.ProgressDialog

import org.reactivestreams.Subscription

import java.lang.ref.WeakReference

import io.reactivex.disposables.Disposable

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/12/27 17:46
 */
abstract class ProgressDialogSubscriber<T> : AndroidSubscriber<T> {
    private var canCancelable = true
    private var mActivity: WeakReference<Activity>? = null
    private var mTitle: String? = null
    private var mMessage: String? = null
    private var dialog: ProgressDialog? = null

    constructor(activity: Activity, title: String, message: String) {
        mActivity = WeakReference(activity)
        mTitle = title
        mMessage = message
    }

    constructor(activity: Activity, title: String, message: String, canCancelable: Boolean) {
        mActivity = WeakReference(activity)
        mTitle = title
        mMessage = message
        this.canCancelable = canCancelable
    }

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        if (mActivity!!.get() != null) {
            dialog = ProgressDialog.show(mActivity!!.get(), mTitle, mMessage)
            dialog?.setCancelable(canCancelable)
            dialog?.setCanceledOnTouchOutside(canCancelable)
            dialog?.setOnCancelListener {
                cancel()
            }
        }
    }

    override fun onSubscribe(s: Subscription) {
        super.onSubscribe(s)
        if (mActivity!!.get() != null) {
            dialog = ProgressDialog.show(mActivity!!.get(), mTitle, mMessage)
            dialog!!.setCancelable(canCancelable)
            dialog!!.setCanceledOnTouchOutside(canCancelable)
            dialog?.setOnCancelListener {
                cancel()
            }
        }
    }


    override fun onNext(t: T) {
        super.onNext(t)
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        if (dialog != null) {
            dialog!!.dismiss()
        }
        mActivity = null
    }

    override fun onComplete() {
        super.onComplete()
        if (dialog != null) {
            dialog!!.dismiss()
        }
        mActivity = null
    }
}

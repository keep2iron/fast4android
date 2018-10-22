package io.github.keep2iron.android.core

import android.support.annotation.CallSuper
import org.reactivestreams.Subscription
import io.github.keep2iron.android.comp.widget.PageStateLayout
import io.github.keep2iron.network.exception.NoDataException
import io.reactivex.disposables.Disposable

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/02/23 11:58
 */
class PageStateSubscriber<T>(private val mPageStateLayout: PageStateLayout) : AndroidSubscriber<T>() {

    override fun onSubscribe(s: Subscription) {
        super.onSubscribe(s)

        mPageStateLayout.displayLoading()
    }

    override fun onSubscribe(disposable: Disposable) {
        super.onSubscribe(disposable)

        mPageStateLayout.displayLoading()
    }

    @CallSuper
    override fun onSuccess(t: T) {
        mPageStateLayout.displayOriginView()

        if (t is List<*>) {
            val list = t as List<*>?
            if (list!!.isEmpty()) {
                mPageStateLayout.displayNoData()
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)

        if (throwable is NoDataException) {
            mPageStateLayout.displayNoData()
        } else {
            mPageStateLayout.displayLoadError()
        }
    }
}

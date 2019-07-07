package io.github.keep2iron.android.load

import io.github.keep2iron.android.databinding.PageStateObservable
import io.github.keep2iron.android.widget.PageState
import io.github.keep2iron.pomelo.AndroidSubscriber

open class RefreshLoadMoreSubscriber<T>(private val adapter: RefreshWithLoadMoreAdapter,
                                        private val pageState: PageStateObservable? = null,
                                        private inline val testRespEmpty: (resp: T) -> Boolean,
                                        block: AndroidSubscriber<T>.() -> Unit)
    : AndroidSubscriber<T>(block) {

    override fun onNext(resp: T) {
        super.onNext(resp)
        adapter.refreshAble.setRefreshEnable(true)
        adapter.loadMoreAble.setLoadMoreEnable(true)

        adapter.refreshAble.showRefreshComplete()
        adapter.loadMoreAble.showLoadMoreComplete()

        val pager = adapter.pager

        try {
            if (testRespEmpty(resp)) {
                if (pager.value == pager.defaultValue) {
                    adapter.recyclerView.post {
                        adapter.recyclerView.scrollToPosition(0)
                    }
                    pageState?.setPageState(PageState.NO_DATA)
                }
                super.onNext(resp)
                throw NoDataException()
            } else {
                if (pager.value == pager.defaultValue) {
                    adapter.recyclerView.post {
                        adapter.recyclerView.scrollToPosition(0)
                    }
                    pageState?.setPageState(PageState.ORIGIN)
                }
                super.onNext(resp)
            }
        } catch (exp: NoDataException) {
            adapter.refreshAble.setRefreshEnable(true)
            adapter.loadMoreAble.showLoadMoreEnd()
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        val pager = adapter.pager
        if (pager.value == pager.defaultValue) {
            pageState?.setPageState(PageState.LOAD_ERROR)
        }

        adapter.loadMoreAble.setLoadMoreEnable(true)
        adapter.refreshAble.setRefreshEnable(true)
        adapter.refreshAble.showRefreshComplete()
        adapter.loadMoreAble.showLoadMoreFailed()
    }

}
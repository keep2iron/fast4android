package io.github.keep2iron.android.load

import android.support.v7.widget.RecyclerView
import android.view.View
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.github.keep2iron.android.adapter.LoadMoreAdapter
import io.github.keep2iron.pomelo.AndroidSubscriber
import io.github.keep2iron.pomelo.exception.NoDataException

interface RefreshLoadListener {
    fun onLoad(adapters: RefreshWithLoadMoreAdapter, index: Int)

    fun onLoadError(e: Throwable) {

    }
}

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 17:00
 *
 * 用于代理Refresh和LoadMore的Adapter
 */
class RefreshWithLoadMoreAdapter private constructor(recyclerView: RecyclerView,
                                                     refreshLayout: View) {

    companion object {
        /**
         * 全局初始化默認參數
         */
        var DEFAULT_INDEX = 0
    }

    private lateinit var loadMoreAble: LoadMoreAble
    private lateinit var refreshAble: Refreshable
    private var loadMoreAdapter: LoadMoreAdapter
    private var index: Int = 0
    private var onLoadListener: RefreshLoadListener? = null
    private var defaultIndex: Int = -1

    init {
        loadMoreAdapter = LoadMoreAdapter(recyclerView.context, recyclerView, false) {
            refreshAble.setRefreshEnable(false)
            onLoadListener?.onLoad(RLDelegateAdapter@ this, index)
        }
        refreshAble = SmartRefreshAble(refreshLayout as SmartRefreshLayout) {
            index = if (-1 == defaultIndex) DEFAULT_INDEX else defaultIndex
            loadMoreAble.setLoadMoreEnable(false)
            onLoadListener?.onLoad(RLDelegateAdapter@ this, index)
        }
        loadMoreAble = VLayoutLoadMoreAble(loadMoreAdapter)
    }

    abstract class Subscriber<T>(private val adapter: RefreshWithLoadMoreAdapter) : AndroidSubscriber<T>() {

        abstract fun doOnSuccess(resp: T)

        override fun onSuccess(resp: T) {
            adapter.refreshAble.setRefreshEnable(true)
            adapter.loadMoreAble.setLoadMoreEnable(true)

            adapter.refreshAble.showRefreshComplete()
            adapter.loadMoreAble.showLoadMoreComplete()
            adapter.index++

            try {
                doOnSuccess(resp)
            } catch (exp: NoDataException) {
                adapter.refreshAble.setRefreshEnable(true)
                adapter.loadMoreAble.showLoadMoreEnd()
            }
        }

        override fun onError(throwable: Throwable) {
            if (throwable is NoDataException) {
                adapter.refreshAble.setRefreshEnable(true)
                adapter.loadMoreAble.showLoadMoreEnd()
                return
            }

            adapter.onLoadListener?.onLoadError(throwable)

            adapter.loadMoreAble.setLoadMoreEnable(true)
            adapter.refreshAble.setRefreshEnable(true)
            adapter.refreshAble.showRefreshComplete()
            adapter.loadMoreAble.showLoadMoreFailed()
        }

    }

    class Builder(recyclerView: RecyclerView,
                  refreshLayout: View) {
        private var adapter: RefreshWithLoadMoreAdapter = RefreshWithLoadMoreAdapter(recyclerView, refreshLayout)

        fun defaultIndexer(defaultIndex: Int): Builder {
            adapter.index = defaultIndex
            adapter.defaultIndex = defaultIndex
            return this
        }

        fun setOnLoadListener(listener: RefreshLoadListener): Builder {
            adapter.onLoadListener = listener
            return this
        }

        fun build(): LoadMoreAdapter = adapter.loadMoreAdapter
    }
}
package io.github.keep2iron.android.load

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.github.keep2iron.android.adapter.AbstractLoadMoreAdapter
import io.github.keep2iron.pomelo.AndroidSubscriber
import io.github.keep2iron.pomelo.exception.NoDataException

interface RefreshLoadListener {
    fun onLoad(adapters: RefreshWithLoadMoreAdapter, pager: Pager)

    fun defaultValue(): Any {
        return 0
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
                                                     refreshLayout: View,
                                                     clazz: Class<out AbstractLoadMoreAdapter>) {

    companion object {
        /**
         * 全局初始化默認參數
         */
        var DEFAULT_INDEX = 0
    }

    private var loadMoreAdapter: AbstractLoadMoreAdapter
    private lateinit var loadMoreAble: LoadMoreAble
    private lateinit var refreshAble: Refreshable
    private var pager: Pager = Pager(DEFAULT_INDEX)
    private var onLoadListener: RefreshLoadListener? = null


    init {
        val constructor = clazz.getConstructor(Context::class.java, RecyclerView::class.java)
        loadMoreAdapter = constructor.newInstance(recyclerView.context, recyclerView) as AbstractLoadMoreAdapter
        loadMoreAdapter.isEnableLoadMore = false
        loadMoreAdapter.mOnLoadMoreListener = {
            refreshAble.setRefreshEnable(false)
            onLoadListener?.onLoad(RLDelegateAdapter@ this, pager)
        }
        refreshAble = SmartRefreshAble(refreshLayout as SmartRefreshLayout) {
            pager.reset()
            loadMoreAble.setLoadMoreEnable(false)
            onLoadListener?.onLoad(RLDelegateAdapter@ this, pager)
        }
        loadMoreAble = VLayoutLoadMoreAble(loadMoreAdapter)
    }

    abstract class Subscriber<T>(private val adapter: RefreshWithLoadMoreAdapter) : AndroidSubscriber<T>() {

        open fun doOnSuccess(resp: T, pager: Pager) {
            pager.value = (pager.value as Int).inc()
        }

        override fun onSuccess(resp: T) {
            adapter.refreshAble.setRefreshEnable(true)
            adapter.loadMoreAble.setLoadMoreEnable(true)

            adapter.refreshAble.showRefreshComplete()
            adapter.loadMoreAble.showLoadMoreComplete()

            try {
                doOnSuccess(resp, adapter.pager)
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

            adapter.loadMoreAble.setLoadMoreEnable(true)
            adapter.refreshAble.setRefreshEnable(true)
            adapter.refreshAble.showRefreshComplete()
            adapter.loadMoreAble.showLoadMoreFailed()
        }

    }

    class Builder(recyclerView: RecyclerView,
                  refreshLayout: View,
                  clazz: Class<out AbstractLoadMoreAdapter>) {
        val context: Context = recyclerView.context.applicationContext

        private var adapter: RefreshWithLoadMoreAdapter = RefreshWithLoadMoreAdapter(recyclerView, refreshLayout, clazz)

        fun setOnLoadListener(listener: RefreshLoadListener): Builder {
            adapter.onLoadListener = listener
            adapter.pager.defaultValue = listener.defaultValue()
            return this
        }

        fun build(): AbstractLoadMoreAdapter = adapter.loadMoreAdapter
    }
}
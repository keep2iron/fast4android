package io.github.keep2iron.android.load

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.github.keep2iron.android.adapter.AbstractLoadMoreAdapter


/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 17:00
 *
 * 用于代理Refresh和LoadMore的Adapter
 */
class RefreshWithLoadMoreAdapter private constructor(val recyclerView: RecyclerView,
                                                     refreshLayout: View,
                                                     clazz: Class<out AbstractLoadMoreAdapter>) {

    var loadMoreAdapter: AbstractLoadMoreAdapter
    var pager: Pager = Pager(0)

    internal lateinit var loadMoreAble: LoadMoreAble
    internal lateinit var refreshAble: Refreshable
    private var onLoadListener: RefreshLoadListener? = null

    fun loadMoreComplete() {
        loadMoreAdapter.showLoadMoreComplete()
    }

    fun loadMoreEnd() {
        loadMoreAdapter.showLoadMoreEnd()
    }

    fun loadMoreFailed() {
        loadMoreAdapter.showLoadMoreFailed()
    }

    fun loadMoreEnabled(isEnabled: Boolean) {
        loadMoreAble.setLoadMoreEnable(isEnabled)
    }

    fun refreshEnabled(isEnabled: Boolean) {
        refreshAble.setRefreshEnable(isEnabled)
    }

    fun refreshComplete() {
        refreshAble.showRefreshComplete()
    }

    init {
        val constructor = clazz.getConstructor(Context::class.java, RecyclerView::class.java)
        loadMoreAdapter = constructor.newInstance(recyclerView.context, recyclerView) as AbstractLoadMoreAdapter
        loadMoreAdapter.isEnableLoadMore = false
        loadMoreAdapter.mOnLoadMoreListener = {
            refreshAble.setRefreshEnable(false)
            onLoadListener?.onLoad(this, pager)
        }
        refreshAble = SmartRefreshAble(refreshLayout as SmartRefreshLayout) {
            pager.reset()
            loadMoreAble.setLoadMoreEnable(false)
            onLoadListener?.onLoad(this, pager)
        }
        loadMoreAble = VLayoutLoadMoreAble(loadMoreAdapter)
    }


    class Builder(recyclerView: RecyclerView,
                  refreshLayout: View,
                  clazz: Class<out AbstractLoadMoreAdapter>) {
        val context: Context = recyclerView.context.applicationContext

        private var adapter: RefreshWithLoadMoreAdapter = RefreshWithLoadMoreAdapter(recyclerView, refreshLayout, clazz)

        fun setOnLoadListener(listener: RefreshLoadListener): Builder {
            adapter.onLoadListener = listener
            val defaultValue = listener.defaultValue()
            adapter.pager.defaultValue = defaultValue
            adapter.pager.value = defaultValue
            return this
        }

        fun build(): RefreshWithLoadMoreAdapter = adapter
    }
}
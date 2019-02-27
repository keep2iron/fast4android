package io.github.keep2iron.android.load

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.github.keep2iron.android.adapter.AbstractLoadMoreAdapter
import io.github.keep2iron.android.databinding.PageStateObservable
import io.github.keep2iron.android.widget.PageState
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
class RefreshWithLoadMoreAdapter private constructor(val recyclerView: RecyclerView,
                                                     refreshLayout: View,
                                                     clazz: Class<out AbstractLoadMoreAdapter>) {

    var loadMoreAdapter: AbstractLoadMoreAdapter
    var pager: Pager = Pager(0)

    private lateinit var loadMoreAble: LoadMoreAble
    private lateinit var refreshAble: Refreshable
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

    fun refreshEnabled(isEnabled:Boolean){
        refreshAble.setRefreshEnable(isEnabled)
    }

    fun refreshComplete(){
        refreshAble.showRefreshComplete()
    }

    init {
        val constructor = clazz.getConstructor(Context::class.java, RecyclerView::class.java)
        loadMoreAdapter = constructor.newInstance(recyclerView.context, recyclerView) as AbstractLoadMoreAdapter
        loadMoreAdapter.isEnableLoadMore = false
        loadMoreAdapter.mOnLoadMoreListener = {
            refreshAble.setRefreshEnable(false)
            onLoadListener?.onLoad( this, pager)
        }
        refreshAble = SmartRefreshAble(refreshLayout as SmartRefreshLayout) {
            pager.reset()
            loadMoreAble.setLoadMoreEnable(false)
            onLoadListener?.onLoad(this, pager)
        }
        loadMoreAble = VLayoutLoadMoreAble(loadMoreAdapter)
    }

    abstract class Subscriber<T>(private val adapter: RefreshWithLoadMoreAdapter,
                                 private val pageState: PageStateObservable? = null) : AndroidSubscriber<T>() {

        abstract fun testRespEmpty(resp: T): Boolean

        open fun doOnSuccess(resp: T, pager: Pager) {
            pager.value = (pager.value as Int).inc()
        }

        override fun onSuccess(resp: T) {
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
                    doOnSuccess(resp, pager)
                    throw NoDataException()
                } else {
                    if (pager.value == pager.defaultValue) {
                        adapter.recyclerView.post {
                            adapter.recyclerView.scrollToPosition(0)
                        }
                        pageState?.setPageState(PageState.ORIGIN)
                    }
                    doOnSuccess(resp, pager)
                }
            } catch (exp: NoDataException) {
                adapter.refreshAble.setRefreshEnable(true)
                adapter.loadMoreAble.showLoadMoreEnd()
            }
        }

        override fun onError(throwable: Throwable) {
            Logger.e(Log.getStackTraceString(throwable))
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
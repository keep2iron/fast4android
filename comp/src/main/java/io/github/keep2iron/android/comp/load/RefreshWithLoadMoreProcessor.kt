package io.github.keep2iron.android.comp.load

import android.support.v7.widget.RecyclerView
import android.view.View
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.github.keep2iron.android.comp.adapter.LoadMoreAdapter
import io.github.keep2iron.pomelo.AndroidSubscriber
import io.github.keep2iron.pomelo.exception.NoDataException

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 17:00
 *
 * 用于代理Refresh和LoadMore的Adapter
 */
class RefreshWithLoadMoreProcessor private constructor(recyclerView: RecyclerView,
                                                       refreshLayout: View) {

    private lateinit var loadMoreAble: LoadMoreAble
    private lateinit var refreshAble: Refreshable
    private var loadMoreAdapter: LoadMoreAdapter
    private var index: Int = 0
    private var onLoadListener: ((adapter: RefreshWithLoadMoreProcessor, pageIndex: Int) -> Unit)? = null
    private var onLoadFailedListener: (() -> Unit)? = null

    private var defaultIndex: Int = 0

    init {
        loadMoreAdapter = LoadMoreAdapter(recyclerView.context, recyclerView) {
            refreshAble.setRefreshEnable(false)
            onLoadListener?.invoke(RLDelegateAdapter@ this, index)
        }
        refreshAble = SmartRefreshAble(refreshLayout as SmartRefreshLayout) {
            index = defaultIndex
            onLoadListener?.invoke(RLDelegateAdapter@ this, index)
        }
        loadMoreAble = VLayoutLoadMoreAble(loadMoreAdapter)
    }

    class Subscriber<T>(private val processor: RefreshWithLoadMoreProcessor) : AndroidSubscriber<T>() {

        override fun onSuccess(resp: T) {
            processor.refreshAble.setRefreshEnable(true)
            processor.loadMoreAble.setLoadMoreEnable(true)

            processor.refreshAble.showRefreshComplete()
            processor.loadMoreAble.showLoadMoreComplete()
            processor.index++
        }

        override fun onError(throwable: Throwable) {
            super.onError(throwable)
            if (throwable is NoDataException) {
                processor.refreshAble.setRefreshEnable(true)
                processor.loadMoreAble.showLoadMoreEnd()
                return
            }

            processor.onLoadFailedListener?.invoke()

            processor.loadMoreAble.setLoadMoreEnable(true)
            processor.refreshAble.setRefreshEnable(true)
            processor.refreshAble.showRefreshComplete()
            processor.loadMoreAble.showLoadMoreFailed()
        }

    }

    class Builder(recyclerView: RecyclerView,
                  refreshLayout: View) {
        private var processor: RefreshWithLoadMoreProcessor = RefreshWithLoadMoreProcessor(recyclerView, refreshLayout)

        fun defaultIndexer(defaultIndex: Int): Builder {
            processor.index = defaultIndex
            processor.defaultIndex = defaultIndex
            return this
        }

        fun setOnLoadListener(onLoadListener: (processor: RefreshWithLoadMoreProcessor, index: Int) -> Unit): Builder {
            processor.onLoadListener = onLoadListener
            return this
        }

        fun setOnLoadFailedListener(onLoadFailedListener: () -> Unit): Builder {
            processor.onLoadFailedListener = onLoadFailedListener
            return this
        }

        fun build(): LoadMoreAdapter = processor.loadMoreAdapter
    }
}
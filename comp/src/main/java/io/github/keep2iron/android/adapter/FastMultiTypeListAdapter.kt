package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.ObservableArrayList
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.load.RefreshLoadListener

class FastMultiTypeListAdapter(context: Context, val data: ObservableArrayList<Any>) : FastListCreator(context) {
    private val multiTypeAdapter: MultiTypeAdapter = MultiTypeAdapter(data)

    init {
        adapters.add(multiTypeAdapter)
    }

    fun setViewMaxCount(viewType: Int, maxCount: Int): FastMultiTypeListAdapter {
        viewPool.setMaxRecycledViews(viewType, maxCount)
        return this
    }

    fun setOnLoadListener(listener: RefreshLoadListener): FastMultiTypeListAdapter {
        this.listener = listener
        return this
    }

    fun addAdapter(adapter: MultiTypeAdapter.SubMultiTypeAdapter<*>): FastMultiTypeListAdapter {
        multiTypeAdapter.registerAdapter(adapter)
        return this
    }

    fun loadMoreClass(loadMoreClass: Class<out AbstractLoadMoreAdapter>): FastMultiTypeListAdapter {
        this.loadMoreClass = loadMoreClass
        return this
    }

    fun useWrapperLayoutManager(): FastMultiTypeListAdapter {
        this.useWrapperLayoutManager = true
        return this
    }

    fun hasConsistItemType(): FastMultiTypeListAdapter {
        this.hasConsistItemType = true
        return this
    }

}
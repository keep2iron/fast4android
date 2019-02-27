package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.load.RefreshLoadListener

class FastMultiTypeListAdapter(context: Context, val data: ObservableList<Any>) : FastListCreator(context) {
    val multiAdapter: MultiTypeAdapter = MultiTypeAdapter(data)

    init {
        adapters.add(multiAdapter)
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
        multiAdapter.registerAdapter(adapter)
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
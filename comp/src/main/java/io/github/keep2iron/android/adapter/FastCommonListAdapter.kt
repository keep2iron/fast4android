package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.ObservableArrayList
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.load.RefreshLoadListener

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/12/4
 */
class FastCommonListAdapter(context: Context) : FastListCreator(context) {

    fun setViewMaxCount(viewType: Int, maxCount: Int): FastCommonListAdapter {
        viewPool.setMaxRecycledViews(viewType, maxCount)
        return this
    }

    fun setOnLoadListener(listener: RefreshLoadListener): FastCommonListAdapter {
        this.listener = listener
        return this
    }

    fun addAdapter(adapter: DelegateAdapter.Adapter<*>): FastCommonListAdapter {
        adapters.add(adapter)
        return this
    }

    fun loadMoreClass(loadMoreClass: Class<out AbstractLoadMoreAdapter>): FastCommonListAdapter {
        this.loadMoreClass = loadMoreClass
        return this
    }

    fun useWrapperLayoutManager(): FastCommonListAdapter {
        this.useWrapperLayoutManager = true
        return this
    }

    fun hasConsistItemType(): FastCommonListAdapter {
        this.hasConsistItemType = true
        return this
    }
}
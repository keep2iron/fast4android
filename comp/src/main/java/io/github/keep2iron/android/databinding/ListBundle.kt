package io.github.keep2iron.android.databinding

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import io.github.keep2iron.android.adapter.AbstractLoadMoreAdapter
import io.github.keep2iron.android.adapter.LoadMoreAdapter
import io.github.keep2iron.android.adapter.WrapperVirtualLayoutManager
import io.github.keep2iron.android.load.RefreshLoadListener

data class ListBundle(val refreshLayout: View,
                      val adapters: ArrayList<DelegateAdapter.Adapter<*>>,
                      val refreshLoadListener: RefreshLoadListener,
                      val map: Map<Int, Int>,
                      val loadMoreClass: Class<out AbstractLoadMoreAdapter> = LoadMoreAdapter::class.java
) {
    val virtualLayoutManager = WrapperVirtualLayoutManager(refreshLayout.context.applicationContext)
    val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)

    fun recyclerPool(): RecyclerView.RecycledViewPool {
        val recycledViewPool = RecyclerView.RecycledViewPool()
        map.forEach {
            recycledViewPool.setMaxRecycledViews(it.key, it.value)
        }
        return recycledViewPool
    }
}
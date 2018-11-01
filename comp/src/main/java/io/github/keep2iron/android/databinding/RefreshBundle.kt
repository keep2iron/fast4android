package io.github.keep2iron.android.databinding

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter

data class RefreshBundle(val refreshLayout: View,
                         val adapters: List<AbstractSubAdapter>,
                         val onLoad: (adapters: RefreshWithLoadMoreAdapter, index: Int) -> Unit,
                         val map: Map<Int, Int>) {

    fun recyclerPool(): RecyclerView.RecycledViewPool {
        val recycledViewPool = RecyclerView.RecycledViewPool()
        map.forEach{
            recycledViewPool.setMaxRecycledViews(it.key,it.value)
        }
        return recycledViewPool
    }
}
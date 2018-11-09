package io.github.keep2iron.android.databinding

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.load.RefreshLoadListener

data class ListBundle(val refreshLayout: View,
                      val adapters: ArrayList<DelegateAdapter.Adapter<*>>,
                      val refreshLoadListener: RefreshLoadListener,
                      val map: Map<Int, Int>
) {

    fun recyclerPool(): RecyclerView.RecycledViewPool {
        val recycledViewPool = RecyclerView.RecycledViewPool()
        map.forEach {
            recycledViewPool.setMaxRecycledViews(it.key, it.value)
        }
        return recycledViewPool
    }

}
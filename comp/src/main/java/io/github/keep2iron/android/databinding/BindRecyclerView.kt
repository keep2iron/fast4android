package io.github.keep2iron.android.databinding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter

data class RefreshBundle(val refreshLayout:View,
                         val onLoad:(adatper: RefreshWithLoadMoreAdapter, index:Int)->Unit)

object BindRecyclerView {

    /**
     * @param adapters 綁定的adapter
     * @param refreshBundle 是否開啟Valyout
     * @param recyclerPool 刷新佈局
     */
    @BindingAdapter( "adapters","refreshBundle","recyclerPool",requireAll = false)
    @JvmStatic
    fun <T> bindRecyclerView(recyclerView:RecyclerView,
                             adapters: List<DelegateAdapter.Adapter<RecyclerViewHolder>>?,
                             refreshBundle: RefreshBundle?,
                             recyclerPool: RecyclerView.RecycledViewPool?
    ) {
        val virtualLayoutManager = VirtualLayoutManager(recyclerView.context.applicationContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager, false)
        adapters?.forEach {
            delegateAdapter.addAdapter(it)
        }
        refreshBundle?.apply {
            delegateAdapter.addAdapter(RefreshWithLoadMoreAdapter.Builder(
                    recyclerView,
                    refreshBundle.refreshLayout)
                    .setOnLoadListener(refreshBundle.onLoad)
                    .build())
        }
        recyclerPool?.apply {
            recyclerView.recycledViewPool = this
        }

        recyclerView.layoutManager = virtualLayoutManager
        recyclerView.adapter = delegateAdapter
    }

}
package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import io.github.keep2iron.android.load.RefreshLoadListener
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/12/3
 *
 * 用于实现快速列表构建的Adapter
 */
abstract class FastListCreator internal constructor(val context: Context) {

    companion object {
        fun createCommonAdapter(context: Context): FastCommonListAdapter {
            return FastCommonListAdapter(context)
        }

        fun createMultiTypeAdapter(context: Context,
                                   data: ObservableList<Any>): FastMultiTypeListAdapter {
            return FastMultiTypeListAdapter(context, data)
        }
    }

    protected val viewPool by lazy {
        RecyclerView.RecycledViewPool()
    }

    protected val adapters by lazy {
        ArrayList<DelegateAdapter.Adapter<*>>()
    }

    protected var listener: RefreshLoadListener? = null

    protected var useWrapperLayoutManager: Boolean = false

    protected var hasConsistItemType: Boolean = false

    protected var loadMoreClass: Class<out AbstractLoadMoreAdapter> = LoadMoreAdapter::class.java

    lateinit var delegateAdapter: DelegateAdapter

    lateinit var virtualLayoutManager: VirtualLayoutManager

    lateinit var refreshLoadMoreAdapter: RefreshWithLoadMoreAdapter

    fun getMultiTypeAdapter(): MultiTypeAdapter {
        return if(this is FastMultiTypeListAdapter){
            this.multiAdapter
        }else{
            throw IllegalArgumentException("you must call FastListCreator.createMultiTypeAdapter() method to create multi type adapter")
        }
    }

    fun bind(recyclerView: RecyclerView,
             refreshLayout: View): FastListCreator {
        if (listener == null) {
            throw IllegalArgumentException("you forget call setOnLoadListener,listener is null")
        }

        if (adapters.isEmpty()) {
            throw IllegalArgumentException("you forget call addAdapter,adapters is empty")
        }

        refreshLoadMoreAdapter = RefreshWithLoadMoreAdapter.Builder(recyclerView, refreshLayout, loadMoreClass)
                .setOnLoadListener(listener!!)
                .build()
        adapters.add(refreshLoadMoreAdapter.loadMoreAdapter)

        virtualLayoutManager = if (useWrapperLayoutManager) {
            WrapperVirtualLayoutManager(context.applicationContext)
        } else {
            VirtualLayoutManager(context.applicationContext)
        }
        recyclerView.setRecycledViewPool(viewPool)
        recyclerView.layoutManager = virtualLayoutManager

        delegateAdapter = DelegateAdapter(virtualLayoutManager, hasConsistItemType)
        delegateAdapter.addAdapters(adapters)
        recyclerView.adapter = delegateAdapter

        return this
    }
}
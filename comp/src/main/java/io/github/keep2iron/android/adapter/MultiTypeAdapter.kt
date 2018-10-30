package io.github.keep2iron.android.adapter

import android.support.v4.util.ArrayMap
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/07/03 00:23
 *
 * 用于多类型Adapter
 */
class MultiTypeAdapter(private val data: MutableList<Any>) : DelegateAdapter.Adapter<RecyclerViewHolder>() {
    private val multiTypeAdapter: ArrayList<AbstractSubAdapter> = ArrayList()
    private val clazzMap: SparseArrayCompat<Class<Any>> = SparseArrayCompat()
    private val adapterMap: ArrayMap<Class<Any>, AbstractSubAdapter> = ArrayMap(10)

    /**
     * 通过实体类型来注册渲染的Adapter
     *
     * 如果onBindViewHolder上的实体类型与adapter有对应 那么会触发adapter的render方法
     */
    fun registerAdapter(adapter: AbstractSubAdapter, clazz: Class<Any>) {
        multiTypeAdapter.add(adapter)
        clazzMap.put(adapter.viewType, clazz)
        adapterMap[clazz] = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        multiTypeAdapter.forEach { adapter ->
            if (adapter.viewType == viewType) {
                return adapter.onCreateViewHolder(parent, viewType)
            }
        }
        throw IllegalArgumentException("itemType = $viewType miss match !please add it call registerAdapter()")
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val adapter = getCurrentBindAdapter(position)
        adapter.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return getCurrentBindAdapter(position).viewType
    }

    private fun getCurrentBindAdapter(position: Int): AbstractSubAdapter {
        val item = data[position]
        for (i in 0 until clazzMap.size()) {
            val key = clazzMap.keyAt(i)
            val clazz = clazzMap[key]

            if (item.javaClass == clazz) {
                return adapterMap[clazz]!!
            }
        }
        throw IllegalArgumentException("position : $position item type is miss match !please add it call registerAdapter()")
    }

}
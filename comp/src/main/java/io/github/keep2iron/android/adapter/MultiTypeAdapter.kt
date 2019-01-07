package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.ObservableList
import android.support.v4.util.ArrayMap
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.android.databinding.RecyclerViewChangeAdapter
import kotlin.reflect.KClass

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/07/03 00:23
 *
 * 用于多类型Adapter
 */
class MultiTypeAdapter(private val data: ObservableList<Any>) : DelegateAdapter.Adapter<RecyclerViewHolder>() {
    private val multiTypeAdapter: ArrayList<SubMultiTypeAdapter<*>> = ArrayList()
    private val clazzMap: SparseArrayCompat<Class<*>> = SparseArrayCompat()
    private val adapterMap: ArrayMap<Class<*>, AbstractSubAdapter> = ArrayMap(10)

    init {
        data.addOnListChangedCallback(RecyclerViewChangeAdapter(this))
    }

    /**
     * 通过实体类型来注册渲染的Adapter
     *
     * 如果onBindViewHolder上的实体类型与adapter有对应 那么会触发adapter的render方法
     */
    fun registerAdapter(adapter: SubMultiTypeAdapter<*>) {
        multiTypeAdapter.add(adapter)
        adapter.setOriginList(data)
        val clazz = adapter.genericClass()
        clazzMap.put(adapter.viewType(), clazz)
        adapterMap[clazz] = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        multiTypeAdapter.forEach { adapter ->
            if (adapter.getItemViewType(-1) == viewType) {
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

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int, payloads: MutableList<Any>) {
        val adapter = getCurrentBindAdapter(position)
        adapter.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return getCurrentBindAdapter(position).getItemViewType(position)
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
        throw IllegalArgumentException("position on $position ,item type ${item.javaClass.name} is miss match !please add it call registerAdapter()")
    }


    /**
     * [T]是要注册的数据类型
     */
    abstract class SubMultiTypeAdapter<T : Any> : AbstractSubAdapter() {
        private lateinit var originList: List<Any>

        internal fun setOriginList(originList: List<Any>) {
            this.originList = originList
        }

        override fun getItemCount(): Int = AbstractSubAdapter.ILLEGAL_SIZE

        override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()

        override fun render(holder: RecyclerViewHolder, position: Int) {
            this.render(holder, originList[position] as T, position)
        }

        abstract fun render(holder: RecyclerViewHolder, data: T, position: Int)

        abstract fun genericClass(): Class<T>

        override fun getItemViewType(position: Int): Int {
            return viewType()
        }

        protected fun getOriginList(): List<Any> {
            return this.originList
        }

        abstract fun viewType(): Int
    }
}
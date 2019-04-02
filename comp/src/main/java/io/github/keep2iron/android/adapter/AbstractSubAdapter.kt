package io.github.keep2iron.android.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/17 16:30
 */
abstract class AbstractSubAdapter : DelegateAdapter.Adapter<RecyclerViewHolder> {
    private var listenerMap = ArrayMap<@IdRes Int, (Int) -> Unit>()

    private var viewType: Int = 0

    companion object {
        const val ILLEGAL_SIZE = -1
    }

    private constructor()

    constructor(viewType: Int = 0) : this() {
        this.viewType = viewType
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    /**
     * 获取布局的layout id
     *
     * @return layout id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     *
     * @param holder
     * @param position
     */
    abstract fun render(holder: RecyclerViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context.applicationContext).inflate(getLayoutId(), parent, false)
        var binding: ViewDataBinding? = null
        try {
            binding = DataBindingUtil.bind(view)
        } catch (exception: IllegalArgumentException) {
        }

        val holder = if (binding == null) {
            RecyclerViewHolder(view)
        } else {
            RecyclerViewHolder(binding)
        }
        val rootListener = listenerMap[-1]
        if(rootListener != null){
            holder.itemView.setOnClickListener {
                rootListener.invoke(holder.layoutPosition)
            }
        }
        listenerMap.forEach { entry->
            holder.findViewById<View>(entry.key).setOnClickListener {
                entry.value.invoke(holder.layoutPosition)
            }
        }
        return holder
    }

    /**
     * [id]不传默认为-1则该listener添加到rootItem下面
     */
    fun setOnItemClickListener(@IdRes id: Int? = -1, listener: (Int) -> Unit) {
        listenerMap[id] = listener
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        render(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }
}
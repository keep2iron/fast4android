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
import io.github.keep2iron.android.comp.R

typealias OnItemClickListener = (Int) -> Unit

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/17 16:30
 */
abstract class AbstractSubAdapter : DelegateAdapter.Adapter<RecyclerViewHolder> {
    private var listenerMap = ArrayMap<@IdRes Int, (Int) -> Unit>()

    private var viewType: Int = 0

    internal var cacheMaxViewCount = 1

    companion object {
        const val ILLEGAL_SIZE = -1
    }

    private constructor()

    constructor(viewType: Int = 0, cacheMaxViewCount: Int = 1) : this() {
        this.viewType = viewType
        this.cacheMaxViewCount = cacheMaxViewCount
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

        var viewDataBinding = DataBindingUtil.findBinding<ViewDataBinding>(view)
        if (viewDataBinding != null) {
            viewDataBinding = DataBindingUtil.bind(view)
        }

        val holder = if (viewDataBinding == null) {
            RecyclerViewHolder(view)
        } else {
            RecyclerViewHolder(viewDataBinding)
        }
        val rootListener = listenerMap[-1]
        if (rootListener != null) {
            holder.itemView.setOnClickListener {
                rootListener.invoke(holder.getTag(R.id.comp_position))
            }
        }
        listenerMap.filter { it.key > 0 }
                .forEach { entry ->
                    holder.findViewById<View>(entry.key).setOnClickListener {
                        entry.value.invoke(holder.getTag(R.id.comp_position))
                    }
                }
        return holder
    }

    /**
     * [id]不传默认为-1则该listener添加到rootItem下面
     */
    fun setOnItemClickListener(@IdRes id: Int? = -1, listener: OnItemClickListener) {
        listenerMap[id] = listener
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setTag(R.id.comp_position, position)
        render(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }
}
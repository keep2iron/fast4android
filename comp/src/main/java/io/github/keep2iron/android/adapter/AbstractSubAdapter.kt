package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.AdapterListUpdateCallback
import android.view.LayoutInflater
import android.view.ViewGroup

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/17 16:30
 */
abstract class AbstractSubAdapter : DelegateAdapter.Adapter<RecyclerViewHolder> {

    protected lateinit var context: Context
    protected lateinit var layoutHelper: LayoutHelper


    private var viewType: Int = 0

    companion object {
        const val ILLEGAL_SIZE = -1
    }

    private constructor()

    constructor(context: Context, viewType: Int = 0) : this() {
        this.context = context.applicationContext
        this.layoutHelper = this.onCreateLayoutHelper()
        this.viewType = viewType
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
        val view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false)
        var binding: ViewDataBinding? = null
        try {
            binding = DataBindingUtil.bind(view)
        } catch (exception: IllegalArgumentException) {
        }

        return if (binding == null) {
            RecyclerViewHolder(view)
        } else {
            RecyclerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
//        val params = holder.itemView.layoutParams
//
//        Logger.d("${params!!.width} ${params.height}")
//        val layoutParams = VirtualLayoutManager.LayoutParams(params!!.width, params.height)
//
//        holder.itemView.layoutParams = VirtualLayoutManager.LayoutParams(layoutParams)

        render(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }
}
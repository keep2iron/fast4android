package io.github.keep2iron.android.comp.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/17 16:30
 *
 * 用于多类型的子布局
 * 一个adapter只针对一种布局
 */
abstract class AbstractSubAdapter : DelegateAdapter.Adapter<RecyclerViewHolder> {

    protected lateinit var context: Context
    protected lateinit var layoutHelper: LayoutHelper
    var viewType: Int = 0

    private constructor()

    constructor(context: Context, viewType: Int) : this() {
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
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(context), getLayoutId(), parent, false)

        return if (binding == null) {
            val view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false)
            RecyclerViewHolder(view)
        } else {
            RecyclerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        render(holder, position)
    }

    final override fun getItemViewType(position: Int): Int {
        return viewType
    }
}
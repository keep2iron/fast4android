package io.github.keep2iron.android.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.android.comp.R
import io.github.keep2iron.android.utilities.WeakHandler

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/17
 *
 * 加载更多adapter的父类 后面一些样式上的问题 可以直接继承该adapter
 *
 */
abstract class AbstractLoadMoreAdapter constructor(context: Context,
                                                   recyclerView: RecyclerView,
                                                   private val mOnLoadMoreListener: (adapter: AbstractLoadMoreAdapter) -> Unit,
                                                   isEnableLoadMore: Boolean = true)
    : AbstractSubAdapter(context.applicationContext) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    private var mCurrentShowState = STATE_DEFAULT
    private var isEnableLoadMore = isEnableLoadMore
    /**
     * 设置距离底部还有preLoadNumber个item就进行预加载
     */
    var preLoadNumber = 0
        set(value) {
            if (value < 0) {
                throw IllegalArgumentException("you set $value for preLoadNumber and it is illegal,you must set it >= 0")
            }
            field = value
        }

    override fun getItemCount(): Int {
        return 1
    }


    init {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (mCurrentShowState == STATE_LOADING) {
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_SETTLING ||
                        newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val isBottom: Boolean
                    val layoutManager = recyclerView.layoutManager
                    isBottom = when (layoutManager) {
                        is LinearLayoutManager -> {
                            layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1 - preLoadNumber
                        }
                        is StaggeredGridLayoutManager -> {
                            val into = IntArray(layoutManager.spanCount)
                            layoutManager.findLastVisibleItemPositions(into)
                            last(into) >= layoutManager.getItemCount() - 1
                        }
                        else -> (layoutManager as GridLayoutManager).findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
                    }
                    if (isBottom && isEnableLoadMore && mCurrentShowState == STATE_DEFAULT) {
                        showLoading()
                        mOnLoadMoreListener(this@AbstractLoadMoreAdapter)
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
    }

    fun setEnableLoadMore(isEnableLoadMore: Boolean) {
        this.isEnableLoadMore = isEnableLoadMore
        mCurrentShowState = STATE_DEFAULT
        notifyItemChanged(0)
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
        val binding = holder.itemView
        binding.findViewById<View>(R.id.load_more_load_fail_view).setOnClickListener {
            mOnLoadMoreListener(this@AbstractLoadMoreAdapter)
            showLoading()
        }
        if (isEnableLoadMore && mCurrentShowState == STATE_DEFAULT) {
            mCurrentShowState = STATE_LOADING
//            Logger.e("${mCurrentShowState} ${isEnableLoadMore}")
            holder.itemView.post {
                mOnLoadMoreListener(this@AbstractLoadMoreAdapter)
            }
        }
        when (mCurrentShowState) {
            STATE_DEFAULT -> {
                this.visibleLoading(binding, false)
                this.visibleLoadFail(binding, false)
                this.visibleLoadEnd(binding, false)
            }
            STATE_LOADING -> {
                this.visibleLoading(binding, true)
                this.visibleLoadFail(binding, false)
                this.visibleLoadEnd(binding, false)
            }
            STATE_LOAD_MORE_FAILED -> {
                this.visibleLoading(binding, false)
                this.visibleLoadFail(binding, true)
                this.visibleLoadEnd(binding, false)
            }
            STATE_LOAD_MORE_END -> {
                this.visibleLoading(binding, false)
                this.visibleLoadFail(binding, false)
                this.visibleLoadEnd(binding, true)
            }
        }
    }

    open fun visibleLoading(binding: View, visible: Boolean) {
        binding.findViewById<View>(R.id.load_more_loading_view).visibility = if (visible) View.VISIBLE else View.GONE
    }

    open fun visibleLoadFail(binding: View, visible: Boolean) {
        binding.findViewById<View>(R.id.load_more_load_fail_view).visibility = if (visible) View.VISIBLE else View.GONE
    }

    open fun visibleLoadEnd(binding: View, visible: Boolean) {
        binding.findViewById<View>(R.id.load_more_load_end_view).visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_TYPE
    }

    fun showLoadMoreFailed() {
        mCurrentShowState = STATE_LOAD_MORE_FAILED
        notifyItemChanged(0)
    }

    fun showLoadMoreEnd() {
        isEnableLoadMore = false
        mCurrentShowState = STATE_LOAD_MORE_END
        notifyItemChanged(0)
    }

    private fun showLoading() {
        mCurrentShowState = STATE_LOADING
        notifyItemChanged(0)
    }

    fun showLoadMoreComplete() {
        mCurrentShowState = STATE_DEFAULT
        notifyItemChanged(0)
    }

    companion object {
        private const val STATE_DEFAULT = 1
        private const val STATE_LOADING = 2
        private const val STATE_LOAD_MORE_FAILED = 3
        private const val STATE_LOAD_MORE_END = 4

        const val ITEM_TYPE = 10001

        /**
         * 取到最后的一个节点
         */
        private fun last(lastPositions: IntArray): Int {
            return lastPositions.max()
                    ?: lastPositions[0]
        }
    }
}
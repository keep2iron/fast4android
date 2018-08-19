package io.github.keep2iron.android.comp.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.alibaba.android.vlayout.LayoutHelper

import com.alibaba.android.vlayout.layout.LinearLayoutHelper

import io.github.keep2iron.android.R

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/01/24 10:48
 */
class LoadMoreAdapter constructor(context: Context,
                                  recyclerView: RecyclerView,
                                  private val mOnLoadMoreListener: (adapter: LoadMoreAdapter) -> Unit
) : AbstractSubAdapter(context.applicationContext,30003) {
    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()

    private var mCurrentShowState = STATE_DEFAULT
    private var isEnableLoadMore = true

    override fun getItemCount(): Int {
        return 1
    }

    override fun getLayoutId(): Int {
        return R.layout.item_load_more
    }

    init {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (mCurrentShowState == STATE_LOADING) {
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val isBottom: Boolean
                    val layoutManager = recyclerView!!.layoutManager
                    isBottom = when (layoutManager) {
                        is LinearLayoutManager -> {
                            layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
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
                        mOnLoadMoreListener(this@LoadMoreAdapter)
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
    }

    fun setEnableLoadMore(isEnableLoadMore: Boolean) {
        this.isEnableLoadMore = isEnableLoadMore
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
        val binding = holder.itemView
        binding.findViewById<View>(R.id.load_more_load_fail_view).setOnClickListener {
            mOnLoadMoreListener(this@LoadMoreAdapter)
            showLoading()
        }
        when (mCurrentShowState) {
            STATE_DEFAULT -> {
                this.visibleLoading(binding, true)
                this.visibleLoadFail(binding, false)
                this.visibleLoadEnd(binding, false)

                mCurrentShowState = STATE_LOADING
                mOnLoadMoreListener(this@LoadMoreAdapter)
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
            else -> {
                this.visibleLoading(binding, false)
                this.visibleLoadFail(binding, false)
                this.visibleLoadEnd(binding, false)
            }
        }
    }

    private fun visibleLoading(binding: View, visible: Boolean) {
        binding.findViewById<View>(R.id.load_more_loading_view).visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun visibleLoadFail(binding: View, visible: Boolean) {
        binding.findViewById<View>(R.id.load_more_load_fail_view).visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun visibleLoadEnd(binding: View, visible: Boolean) {
        binding.findViewById<View>(R.id.load_more_load_end_view).visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showLoadMoreFailed() {
        mCurrentShowState = STATE_LOAD_MORE_FAILED
        notifyItemChanged(0)
    }

    fun showLoadMoreEnd() {
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

        /**
         * 取到最后的一个节点
         */
        private fun last(lastPositions: IntArray): Int {
            return lastPositions.max()
                    ?: lastPositions[0]
        }
    }
}
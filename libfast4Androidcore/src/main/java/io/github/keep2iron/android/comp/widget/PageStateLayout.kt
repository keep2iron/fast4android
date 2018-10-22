package io.github.keep2iron.android.comp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

import io.github.keep2iron.android.R

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.1
 * @since 2018/03/08 17:26
 *
 * 状态管理layout，替代之前的PageStateManager，可能是Dagger2的思想
 */
class PageStateLayout constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr){
    /**
     * 被状态管理的View
     */
    private var mOriginView: View? = null
    /**
     * 无数据view
     */
    private var mNoDataView: View? = null
    /**
     * 无网络
     */
    private var mNoNetwork: View? = null
    /**
     * 加载失败
     */
    private var mLoadError: View? = null
    /**
     * 正在加载
     */
    private var mLoadingView: View? = null

    init {

        val array = resources.obtainAttributes(attrs, R.styleable.PageStateLayout)

        for (i in 0 until array.indexCount) {
            val index = array.getIndex(i)
            when (index) {
                R.styleable.PageStateLayout_psl_load_error_layout -> {
                    mLoadError = LayoutInflater.from(getContext()).inflate(array.getResourceId(index, -1), this, false)
                    mLoadError?.visibility = View.GONE
                }
                R.styleable.PageStateLayout_psl_no_data_layout -> {
                    mNoDataView = LayoutInflater.from(getContext()).inflate(array.getResourceId(index, -1), this, false)
                    mNoDataView?.visibility = View.GONE
                }
                R.styleable.PageStateLayout_psl_no_network_layout -> {
                    mNoNetwork = LayoutInflater.from(getContext()).inflate(array.getResourceId(index, -1), this, false)
                    mNoNetwork?.visibility = View.GONE
                }
                R.styleable.PageStateLayout_psl_loading_layout -> {
                    mLoadingView = LayoutInflater.from(getContext()).inflate(array.getResourceId(index, -1), this, false)
                    mLoadingView?.visibility = View.GONE
                }
            }
        }
        array.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount <= 0) {
            throw IllegalArgumentException(javaClass.simpleName + "'child count must > 0")
        }

        mOriginView = getChildAt(0)

        if (mLoadError != null) {
            addView(mLoadError, 0)
        }

        if (mNoDataView != null) {
            addView(mNoDataView, 0)
        }

        if (mNoNetwork != null) {
            addView(mNoNetwork, 0)
        }

        if (mLoadingView != null) {
            addView(mLoadingView, 0)
        }
    }

    private fun initView() {
        mOriginView?.visibility = View.GONE

        if (mNoDataView != null) {
            mNoDataView?.visibility = View.GONE
        }

        if (mNoNetwork != null) {
            mNoNetwork?.visibility = View.GONE
        }

        if (mLoadError != null) {
            mLoadError?.visibility = View.GONE
        }

        if (mLoadingView != null) {
            mLoadingView?.visibility = View.GONE
        }
    }

    fun displayOriginView() {
        initView()

        mOriginView?.visibility = View.VISIBLE
    }

    fun displayNoNetwork() {
        initView()
        if (mNoNetwork != null) {
            mNoNetwork?.visibility = View.VISIBLE
        }
    }

    fun displayNoData() {
        initView()
        if (mNoDataView != null) {
            mNoDataView?.visibility = View.VISIBLE
        }
    }

    fun displayLoading() {
        initView()
        if (mLoadingView != null) {
            mLoadingView?.visibility = View.VISIBLE
        }
    }

    fun displayLoadError() {
        initView()
        if (mLoadError != null) {
            mLoadError?.visibility = View.VISIBLE
        }
    }
}
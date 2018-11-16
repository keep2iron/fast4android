package io.github.keep2iron.android.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import io.github.keep2iron.android.comp.R

enum class PageState {
    ORIGIN,
    NO_DATA,
    NO_NETWORK,
    LOAD_ERROR,
    LOADING,
}

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.1
 * @since 2018/03/08 17:26
 *
 * 状态管理layout，
 */
class PageStateLayout constructor(context: Context, attrs: AttributeSet? = null)
    : FrameLayout(context, attrs, 0) {
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

    private var pageState = PageState.ORIGIN

    private var views: Array<View?> = Array(5) { null }

    private var duration = 500

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

        views[0] = mOriginView
        views[1] = mLoadError
        views[2] = mNoDataView
        views[3] = mNoNetwork
        views[4] = mLoadingView

        initPageState(pageState)
    }

    private fun animStateView(showView: View?) {
        views.forEach {
            //如果不是要显示的View则进行隐藏
            if (it != null && it != showView && it.visibility == View.VISIBLE) {
                it.animate()
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                it.visibility = View.GONE
                            }
                        })
                        .setDuration(duration.toLong())
                        .alpha(0f)
                        .start()
            } else if (it != null && it == showView && it.visibility == View.GONE) {
                it.alpha = 0f
                it.visibility = View.VISIBLE
                it.animate()
                        .setDuration(duration.toLong())
                        .alpha(1f)
                        .start()
            }
        }
    }

    fun displayOriginView() {
        if(pageState != PageState.ORIGIN) {
            pageState = PageState.ORIGIN
            animStateView(mOriginView)
        }
    }

    fun displayNoNetwork() {
        if(pageState != PageState.NO_NETWORK) {
            pageState = PageState.NO_NETWORK
            if (mNoNetwork == null) {
                throw IllegalArgumentException("you should add no network layout")
            }
            animStateView(mNoNetwork)
        }
    }

    fun displayNoData() {
        if(pageState != PageState.NO_DATA) {
            pageState = PageState.NO_DATA
            if (mNoDataView == null) {
                throw IllegalArgumentException("you should add no data layout")
            }
            animStateView(mNoDataView)
        }
    }

    fun displayLoading() {
        if(pageState != PageState.LOADING) {
            pageState = PageState.LOADING
            if (mLoadingView == null) {
                throw IllegalArgumentException("you should add loading layout")
            }
            animStateView(mLoadingView)
        }
    }

    fun displayLoadError() {
        if(pageState != PageState.LOAD_ERROR) {
            pageState = PageState.LOAD_ERROR
            if (mLoadError == null) {
                throw IllegalArgumentException("you should add load error layout")
            }
            animStateView(mLoadError)
        }
    }

    /**
     * 设置初始化页面状态
     */
    fun initPageState(pageState: PageState) {
        this.pageState = pageState

        //对应views所在的index
        val pageStateArr = arrayOf(PageState.ORIGIN, PageState.LOAD_ERROR, PageState.NO_DATA, PageState.NO_NETWORK, PageState.LOADING)
        val pageStateIndex = pageStateArr.indexOf(pageState)
        views.forEachIndexed { index, view ->
            if (index == pageStateIndex) {
                val stateView = view ?: throw IllegalArgumentException("$pageState view not add you should add state layout id in your xml.")
                stateView.alpha = 1f
                stateView.visibility = View.VISIBLE
            } else {
                view?.alpha = 0f
                view?.visibility = View.GONE
            }
        }
    }
}
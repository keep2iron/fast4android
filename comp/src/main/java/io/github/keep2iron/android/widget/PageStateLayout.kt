package io.github.keep2iron.android.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.v4.util.ArrayMap
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
    var mNoDataView: View? = null
        set(value) {
            field = value
            views[PageState.NO_DATA] = value
        }
    /**
     * 无网络
     */
    var mNoNetwork: View? = null
        set(value) {
            field = value
            views[PageState.NO_NETWORK] = value
        }
    /**
     * 加载失败
     */
    var mLoadError: View? = null
        set(value) {
            field = value
            views[PageState.LOAD_ERROR] = value
        }
    /**
     * 正在加载
     */
    var mLoadingView: View? = null
        set(value) {
            field = value
            views[PageState.LOADING] = value
        }

    private var pageState = PageState.ORIGIN

    private var views: ArrayMap<PageState, View?> = ArrayMap()

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

        views[PageState.ORIGIN] = mOriginView
        views[PageState.LOAD_ERROR] = mLoadError
        views[PageState.NO_DATA] = mNoDataView
        views[PageState.NO_NETWORK] = mNoNetwork
        views[PageState.LOADING] = mLoadingView

        initPageState(pageState)
    }

    /**
     * loaderror -> loading
     */
    private fun animStateView(preState: PageState, showView: View) {
        val preSateView = views[preState]!!

        preSateView.animate().cancel()
        preSateView.visibility = View.VISIBLE
        preSateView.alpha = 1f
        preSateView.animate()
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        preSateView.visibility = View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        super.onAnimationCancel(animation)
                    }
                }).setDuration(duration.toLong())
                .alpha(0f)
                .start()

        showView.animate().cancel()
        showView.alpha = 0f
        showView.visibility = View.VISIBLE
        showView.animate()
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationCancel(animation: Animator) {
                        super.onAnimationCancel(animation)
                    }
                })
                .setDuration(duration.toLong())
                .alpha(1f)
                .start()
    }

    fun isVisible(view: View): Boolean {
        return view.visibility == View.VISIBLE
    }

    fun displayOriginView() {
        if (pageState != PageState.ORIGIN) {
            val preState = pageState
            pageState = PageState.ORIGIN
            animStateView(preState, mOriginView!!)
        }
    }

    fun displayNoNetwork() {
        if (pageState != PageState.NO_NETWORK) {
            val preState = pageState
            pageState = PageState.NO_NETWORK
            if (mNoNetwork == null) {
                throw IllegalArgumentException("you should add no network layout")
            }
            animStateView(preState, mNoNetwork!!)
        }
    }

    fun displayNoData() {
        if (pageState != PageState.NO_DATA) {
            val preState = pageState
            pageState = PageState.NO_DATA
            if (mNoDataView == null) {
                throw IllegalArgumentException("you should add no data layout")
            }
            animStateView(preState, mNoDataView!!)
        }
    }

    fun displayLoading() {
        if (pageState != PageState.LOADING) {
            val preState = pageState
            pageState = PageState.LOADING
            if (mLoadingView == null) {
                throw IllegalArgumentException("you should add loading layout")
            }
            animStateView(preState, mLoadingView!!)
        }
    }

    fun displayLoadError() {
        if (pageState != PageState.LOAD_ERROR) {
            val preState = pageState
            pageState = PageState.LOAD_ERROR
            if (mLoadError == null) {
                throw IllegalArgumentException("you should add load error layout")
            }
            animStateView(preState, mLoadError!!)
        }
    }

    /**
     * 设置初始化页面状态
     */
    fun initPageState(pageState: PageState) {
        this.pageState = pageState


        for (i in 0..views.size) {
            val key = views.keyAt(i)
            val view = views[key]
            if (key == pageState) {
                val stateView = view
                        ?: throw IllegalArgumentException("$pageState view not add you should add state layout id in your xml.")
                stateView.alpha = 1f
                stateView.visibility = View.VISIBLE
            } else {
                view?.let {
                    view.alpha = 0f
                    view.visibility = View.GONE
                }
            }
        }
    }
}
package io.github.keep2iron.app.module.recommend

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bana.bananasays.libaspect.FragmentVisibleDelegate
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.databinding.RefreshBundle
import io.github.keep2iron.android.ext.LifecycleViewModelFactory
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.RecommendFragmentBinding
import io.github.keep2iron.app.ui.Title
import io.github.keep2iron.app.util.Constant
import io.github.keep2iron.app.valyout.BannerAdapter
import io.github.keep2iron.app.valyout.VideoAdapter

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/26 11:16
 *
 * 推荐的Fragment
 */
class RecommendFragment : AbstractFragment<RecommendFragmentBinding>(), Title {
    var delegate: FragmentVisibleDelegate = FragmentVisibleDelegate {
        Logger.d("${this.javaClass.simpleName} show $it")
    }

    override fun initVariables(container: View, savedInstanceState: Bundle?) {
        val recommendModel = ViewModelProviders.of(this, LifecycleViewModelFactory(this)).get(RecommendModel::class.java)

        dataBinding.bundle = RefreshBundle(dataBinding.refreshLayout,
                arrayListOf(
                        BannerAdapter(applicationContext, recommendModel, dataBinding.rvRecyclerView, activity!!.supportFragmentManager),
                        VideoAdapter(applicationContext, recommendModel, activity!!.supportFragmentManager)
                ),
                { adapter: RefreshWithLoadMoreAdapter, index: Int ->
                    recommendModel.processorRefreshWithLoadMore(adapter, index)
                },
                mapOf(
                        Constant.RECYCLE_BANNER_ITEM to 1,
                        Constant.RECYCLE_BANNER_SUB_ITEM to 5,
                        Constant.RECYCLE_VIDEO_ITEM to 20
                )
        )

//        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
//        val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)
//        dataBinding.rvRecyclerView.adapter = delegateAdapter
//        dataBinding.rvRecyclerView.layoutManager = virtualLayoutManager
//        delegateAdapter.addAdapter(BannerAdapter(applicationContext, recommendModel, dataBinding.rvRecyclerView, activity!!.supportFragmentManager))
//        delegateAdapter.addAdapter(VideoAdapter(applicationContext, recommendModel, activity!!.supportFragmentManager))
//        delegateAdapter.addAdapter(RefreshWithLoadMoreAdapter.Builder(
//                dataBinding.rvRecyclerView,
//                dataBinding.refreshLayout)
//                .defaultIndexer(1)
//                .setOnLoadListener { processor, index ->
//                    recommendModel.processorRefreshWithLoadMore(processor, index)
//                }
//                .build())
//        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(dataBinding.rvRecyclerView))
        Logger.d("${this::class.simpleName} initVariables")
    }

    override val resId: Int = R.layout.recommend_fragment

    override fun getTitle(): String = "推荐"

    companion object {
        fun getInstance(): RecommendFragment = RecommendFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Logger.d("onCreateView ${this.javaClass.simpleName}")
        delegate.onCreateView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
//        Logger.d("onResume ${this.javaClass.simpleName}")
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        Logger.d("${this.javaClass.simpleName} setUserVisibleHint $isVisibleToUser")
        super.setUserVisibleHint(isVisibleToUser)
        delegate.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
//        Logger.d("${this.javaClass.simpleName} onHiddenChanged $hidden")
        super.onHiddenChanged(hidden)
        delegate.onHiddenChanged(hidden)
    }
}

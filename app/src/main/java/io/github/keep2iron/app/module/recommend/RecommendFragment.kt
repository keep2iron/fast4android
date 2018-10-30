package io.github.keep2iron.app.module.recommend

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.ext.LifecycleViewModelFactory
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.RecommendFragmentBinding
import io.github.keep2iron.app.ui.Title
import io.github.keep2iron.app.valyout.BannerAdapter
import io.github.keep2iron.app.valyout.VideoAdapter
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/26 11:16
 *
 * 推荐的Fragment
 */
class RecommendFragment : AbstractFragment<RecommendFragmentBinding>(), Title {
    override fun initVariables(container: View, savedInstanceState: Bundle?) {
        val recommendModel = ViewModelProviders.of(this, LifecycleViewModelFactory(this)).get(RecommendModel::class.java)
        Logger.d("savedInstanceState == ${savedInstanceState} ${recommendModel.indexItems.size}")



//        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
//        val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)
//        dataBinding.rvRecyclerView.adapter = delegateAdapter
//        dataBinding.rvRecyclerView.layoutManager = virtualLayoutManager
//        delegateAdapter.addAdapter(BannerAdapter(applicationContext, recommendModel, dataBinding.rvRecyclerView, activity!!.supportFragmentManager))
//        delegateAdapter.addAdapter(VideoAdapter(applicationContext, recommendModel, activity!!.supportFragmentManager))
//        delegateAdapter.addAdapter(RefreshWithLoadMoreAdapter.Builder(
//                dataBinding.rvRecyclerView,
//                dataBinding.srlRefreshLayout)
//                .defaultIndexer(1)
//                .setOnLoadListener { processor, index ->
//                    recommendModel.processorRefreshWithLoadMore(processor, index)
//                }
//                .build())
        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(dataBinding.rvRecyclerView))
        Logger.d("${this::class.simpleName} initVariables")
    }

    override val resId: Int = R.layout.recommend_fragment

    override fun getTitle(): String = "推荐"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d("${this::class.simpleName} onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        Logger.d("${this::class.simpleName} onDestroyView")
        super.onDestroyView()
    }

    override fun onDetach() {
        Logger.d("${this::class.simpleName} onDetach")
        super.onDetach()
    }

    companion object {
        fun getInstance(): RecommendFragment = RecommendFragment()
    }
}

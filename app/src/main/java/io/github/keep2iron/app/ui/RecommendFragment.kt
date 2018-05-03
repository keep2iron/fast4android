package io.github.keep2iron.app.ui

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.google.gson.Gson
import io.github.keep2iron.android.comp.load.RefreshWithLoadMoreProcessor
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.core.LifecycleViewModelFactory
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.RecommendFragmentBinding
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.valyout.BannerAdapter
import io.github.keep2iron.app.valyout.VideoAdapter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/26 11:16
 *
 * 推荐的Fragment
 */
class RecommendFragment : AbstractFragment<RecommendFragmentBinding>(), Title {
    override fun initVariables(container: View?) {
        val recommendModel = ViewModelProviders.of(this, LifecycleViewModelFactory(this)).get(RecommendModel::class.java)

        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)
        dataBinding.rvRecyclerView.adapter = delegateAdapter
        dataBinding.rvRecyclerView.layoutManager = virtualLayoutManager
        delegateAdapter.addAdapter(BannerAdapter(applicationContext, recommendModel, dataBinding.rvRecyclerView))
        delegateAdapter.addAdapter(VideoAdapter(applicationContext, recommendModel))
        delegateAdapter.addAdapter(RefreshWithLoadMoreProcessor.Builder(
                dataBinding.rvRecyclerView,
                dataBinding.srlRefreshLayout)
                .defaultIndexer(1)
                .setOnLoadListener { processor, index ->
                    recommendModel.processorRefreshWithLoadMore(processor, index)
                }
                .build())
        delegateAdapter.notifyDataSetChanged()
    }

    override val resId: Int = R.layout.recommend_fragment

    override fun getTitle(): String = "推荐"

    companion object {
        fun getInstance(): RecommendFragment = RecommendFragment()
    }
}

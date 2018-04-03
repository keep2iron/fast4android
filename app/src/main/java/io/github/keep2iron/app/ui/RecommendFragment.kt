package io.github.keep2iron.app.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import io.github.keep2iron.android.comp.adapter.LoadMoreAdapter
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.core.LifecycleViewModelFactory
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.RecommendFragmentBinding
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
    override fun initVariables(container: View?) {
        val recommendModel = ViewModelProviders.of(this, LifecycleViewModelFactory(this)).get(RecommendModel::class.java)

        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)
        dataBinding.rvRecyclerView.adapter = delegateAdapter
        dataBinding.rvRecyclerView.layoutManager = virtualLayoutManager
        delegateAdapter.addAdapter(BannerAdapter(applicationContext, recommendModel, dataBinding.rvRecyclerView))
        delegateAdapter.addAdapter(VideoAdapter(applicationContext, recommendModel))
        delegateAdapter.addAdapter(LoadMoreAdapter(applicationContext, dataBinding.rvRecyclerView, { adapter ->
            recommendModel.loadData {
                adapter.showLoadMoreComplete()
            }
        }))
        delegateAdapter.notifyDataSetChanged()
        recommendModel.loadData()
    }

    override val resId: Int = R.layout.recommend_fragment

    override fun getTitle(): String = "推荐"

    companion object {
        fun getInstance():RecommendFragment = RecommendFragment()
    }
}

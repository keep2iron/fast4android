package io.github.keep2iron.app.module.recommend

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.comp.ListDataBindingDelegate
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.ext.LifecycleViewModelFactory
import io.github.keep2iron.android.load.RefreshLoadListener
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.RecommendFragmentBinding
import io.github.keep2iron.app.ui.Title
import io.github.keep2iron.app.util.Constant
import io.github.keep2iron.app.valyout.BannerAdapter
import io.github.keep2iron.app.valyout.VideoAdapter
import java.util.ArrayList

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/26 11:16
 *
 * 推荐的Fragment
 */
class RecommendFragment : AbstractFragment<RecommendFragmentBinding>(), Title, ListDataBindingDelegate {
    private lateinit var recommendModel: RecommendModel

    override fun refreshLoadListener(): RefreshLoadListener = recommendModel

    override fun refreshView(): View = dataBinding.refreshLayout

    override fun adapters(): ArrayList<DelegateAdapter.Adapter<*>> = arrayListOf(
            BannerAdapter(applicationContext, recommendModel, dataBinding.rvRecyclerView, activity!!.supportFragmentManager),
            VideoAdapter(applicationContext, recommendModel, activity!!.supportFragmentManager)
    )

    override fun itemTypeMap(): Map<Int, Int> = mapOf(
            Constant.RECYCLE_BANNER_ITEM to 1,
            Constant.RECYCLE_BANNER_SUB_ITEM to 5,
            Constant.RECYCLE_VIDEO_ITEM to 20
    )

    override fun initVariables(container: View, savedInstanceState: Bundle?) {
        recommendModel = ViewModelProviders.of(this, LifecycleViewModelFactory(this)).get(RecommendModel::class.java)
        recommendModel.init(dataBinding.stateLayout)

        val listBundle = buildRefreshBundle()
        dataBinding.bundle = listBundle
        val adapter = listBundle.buildAdapter(dataBinding.rvRecyclerView)

        recommendModel.onLoad(adapter, adapter.pager)
//        dataBinding.refreshLayout.autoRefresh()
    }

    override val resId: Int = R.layout.recommend_fragment

    override fun getTitle(): String = "推荐"

    companion object {
        fun getInstance(): RecommendFragment = RecommendFragment()
    }
}

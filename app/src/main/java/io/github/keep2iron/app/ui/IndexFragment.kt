package io.github.keep2iron.app.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.comp.adapter.AbstractSubAdapter
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import io.github.keep2iron.app.R
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.core.LifecycleViewModelFactory
import io.github.keep2iron.android.core.register
import io.github.keep2iron.app.databinding.IndexFragmentBinding
import io.github.keep2iron.app.valyout.BannerAdapter
import io.github.keep2iron.app.valyout.VideoAdapter

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/08 10:35
 */
class IndexFragment : AbstractFragment<IndexFragmentBinding>() {
    override val resId: Int = R.layout.index_fragment

    override fun initVariables(container: View?) {
        val indexModule = ViewModelProviders.of(this, LifecycleViewModelFactory(this)).get(IndexModule::class.java)

        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)
        dataBinding.rvRecyclerView.adapter = delegateAdapter
        dataBinding.rvRecyclerView.layoutManager = virtualLayoutManager
        delegateAdapter.addAdapter(BannerAdapter(applicationContext, indexModule, dataBinding.rvRecyclerView))
        delegateAdapter.addAdapter(VideoAdapter(applicationContext, indexModule))

        indexModule.loadData()
        delegateAdapter.notifyDataSetChanged()
    }

    companion object {
        fun getInstance(): IndexFragment {
            return IndexFragment()
        }
    }
}
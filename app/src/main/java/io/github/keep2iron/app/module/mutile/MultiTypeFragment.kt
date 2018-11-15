package io.github.keep2iron.app.module.mutile

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.adapter.MultiTypeAdapter
import io.github.keep2iron.android.comp.ListDataBindingDelegate
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.ext.getComponentService
import io.github.keep2iron.android.load.Pager
import io.github.keep2iron.android.load.RefreshLoadListener
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter
import io.github.keep2iron.android.utilities.RxTransUtil.rxObservableScheduler
import io.github.keep2iron.app.Application
import io.github.keep2iron.app.NetworkServiceProvider
import io.github.keep2iron.app.R
import io.github.keep2iron.app.data.remote.ApiService
import io.github.keep2iron.app.databinding.FragmentMutileTypeBinding
import io.github.keep2iron.app.model.*
import io.github.keep2iron.pomelo.NetworkManager
import io.github.keep2iron.pomelo.exception.NoDataException
import java.util.ArrayList

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/9
 */
class MultiTypeFragment : AbstractFragment<FragmentMutileTypeBinding>(), ListDataBindingDelegate, RefreshLoadListener {
    override fun adapters(): ArrayList<DelegateAdapter.Adapter<*>> = arrayListOf(adapter)

    val list = ObservableArrayList<Any>()

    val adapter: MultiTypeAdapter by lazy {
        MultiTypeAdapter(list).apply {
            registerAdapter(Type1Adapter(requireContext()), MultiModel1::class.java)
            registerAdapter(Type2Adapter(requireContext()), MultiModel2::class.java)
            registerAdapter(Type3Adapter(requireContext()), MultiModel3::class.java)
        }
    }

    override fun onLoad(adapters: RefreshWithLoadMoreAdapter, pager: Pager) {
        val networkManager = Application.instance.getComponentService<NetworkManager>(NetworkServiceProvider.NETWORK_MANAGER)
        val apiService = networkManager.getService(ApiService::class.java)
        apiService.multiType(pager.value as Int)
                .compose(rxObservableScheduler())
                .subscribe(object : RefreshWithLoadMoreAdapter.Subscriber<BaseResponse<List<MultiModelWrapper>>>(adapters) {
                    override fun doOnSuccess(resp: BaseResponse<List<MultiModelWrapper>>, pager: Pager) {
                        if (resp.value == null || resp.value!!.isEmpty()) {
                            throw NoDataException()
                        }

                        if (pager.value == 0) {
                            dataBinding.recyclerView.scrollToPosition(0)
                            list.clear()
                        }
                        resp.value?.map {
                            when (it.type) {
                                1 -> {
                                    it.model1
                                }
                                2 -> {
                                    it.model2
                                }
                                3 -> {
                                    it.model3
                                }
                                else -> {
                                }
                            }
                        }?.apply {
                            list.addAll(this)
                        }
                        super.doOnSuccess(resp, pager)
                    }
                })
    }

    override fun refreshView(): View = dataBinding.refreshLayout

    override fun refreshLoadListener(): RefreshLoadListener = this

    override
    val resId: Int = R.layout.fragment_mutile_type

    override fun initVariables(container: View, savedInstanceState: Bundle?) {
        dataBinding.bundle = buildRefreshBundle()
    }

    override fun lazyLoad(container: View?) {
        dataBinding.refreshLayout.autoRefresh()
    }
}
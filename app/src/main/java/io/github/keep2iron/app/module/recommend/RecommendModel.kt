package io.github.keep2iron.app.module.recommend

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import io.github.keep2iron.android.databinding.PageState
import io.github.keep2iron.android.databinding.PageStateObservable
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter
import io.github.keep2iron.android.ext.LifeCycleViewModule
import io.github.keep2iron.android.load.Pager
import io.github.keep2iron.android.load.RefreshLoadListener
import io.github.keep2iron.android.widget.PageStateLayout
import io.github.keep2iron.app.Application
import io.github.keep2iron.app.data.DataRepository
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.pomelo.exception.NoDataException
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.lang.Exception
import java.util.*

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:31
 */
class RecommendModel(owner: LifecycleOwner) : LifeCycleViewModule(Application.instance, owner), RefreshLoadListener {

    var bannerItems: ObservableArrayList<GsonIndex> = ObservableArrayList()
    var indexItems: ObservableArrayList<GsonIndex> = ObservableArrayList()
    lateinit var pageState: PageStateObservable

    fun init(pageStateLayout: PageStateLayout) {
        pageState = PageStateObservable(pageStateLayout, PageState.LOADING)
    }

    fun loadBanner(): Observable<List<GsonIndex>> {
        return DataRepository.instance.indexBanner()
                .compose(bindObservableLifeCycle())
                .doOnNext { resp ->
                    bannerItems.clear()
                    bannerItems.addAll(resp)
                }
    }

    fun loadData(index: Int): Observable<List<GsonIndex>> {
        return DataRepository.instance.indexMovie(index)
                .compose(bindObservableLifeCycle())
                .doOnNext { resp ->
                    if (resp.isEmpty()) {
                        throw NoDataException()
                    }

                    if (index == 0) indexItems.clear()
                    indexItems.addAll(resp)
                }
    }

    override fun onLoad(adapters: RefreshWithLoadMoreAdapter, pager: Pager) {
        if (pager.value == 0) {
            Observable.zip(loadData(pager.value as Int),
                    loadBanner(),
                    BiFunction<List<GsonIndex>, List<GsonIndex>, Any> { _, _ -> Object::class.java })
                    .subscribe(object : RefreshWithLoadMoreAdapter.Subscriber<Any>(adapters) {
                        override fun doOnSuccess(resp: Any) {
                            pageState.setPageState(PageState.ORIGIN)
                        }
                    })
        } else {
            loadData(pager.value as Int)
                    .subscribe(object : RefreshWithLoadMoreAdapter.Subscriber<Any>(adapters) {
                        override fun doOnSuccess(resp: Any) {
                        }
                    })
        }
    }

    override fun onLoadError(e: Throwable) {
        if (e is NoDataException) {
            pageState.setPageState(PageState.NO_DATA)
        } else {
            pageState.setPageState(PageState.LOAD_ERROR)
        }
    }
}
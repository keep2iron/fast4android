package io.github.keep2iron.app.module.recommend

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import io.github.keep2iron.android.comp.load.RefreshWithLoadMoreProcessor
import io.github.keep2iron.android.ext.LifeCycleViewModule
import io.github.keep2iron.app.Application
import io.github.keep2iron.app.data.DataRepository
import io.github.keep2iron.app.model.GsonIndex
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:31
 */
class RecommendModel(owner: LifecycleOwner) : LifeCycleViewModule(Application.instance, owner) {
    var bannerItems: ObservableArrayList<GsonIndex> = ObservableArrayList()
    var indexItems: ObservableArrayList<GsonIndex> = ObservableArrayList()

    init {
//        bannerItems.clear()
//        bannerItems.add(R.drawable.banner01)
//        bannerItems.add(R.drawable.banner02)
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
                    if (index == 1) indexItems.clear()
                    indexItems.addAll(resp)
                }
    }

    fun processorRefreshWithLoadMore(processor: RefreshWithLoadMoreProcessor, index: Int) {
        if (index == 1) {
            Observable.zip(loadData(index),
                    loadBanner(),
                    BiFunction<List<GsonIndex>, List<GsonIndex>, String> { _, _ -> "" })
                    .subscribe(RefreshWithLoadMoreProcessor.Subscriber(processor))
        } else {
            loadData(index)
                    .subscribe(RefreshWithLoadMoreProcessor.Subscriber(processor))
        }
    }
}
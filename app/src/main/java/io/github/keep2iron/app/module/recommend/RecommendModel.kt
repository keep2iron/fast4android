package io.github.keep2iron.app.module.recommend

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import android.view.View
import io.github.keep2iron.android.databinding.PageStateObservable
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter
import io.github.keep2iron.android.ext.LifeCycleViewModule
import io.github.keep2iron.android.load.Pager
import io.github.keep2iron.android.load.RefreshLoadListener
import io.github.keep2iron.android.load.RefreshLoadMoreSubscriber
import io.github.keep2iron.android.widget.PageState
import io.github.keep2iron.android.widget.CompPageStateLayout
import io.github.keep2iron.android.widget.ReloadListener
import io.github.keep2iron.app.Application
import io.github.keep2iron.app.data.DataRepository
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.model.Movie
import io.github.keep2iron.pomelo.exception.NoDataException
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:31
 */
class RecommendModel(owner: LifecycleOwner) : LifeCycleViewModule(Application.instance, owner), RefreshLoadListener, ReloadListener {
    var bannerItems: ObservableArrayList<Movie> = ObservableArrayList()
    var indexItems: ObservableArrayList<Movie> = ObservableArrayList()
    lateinit var pageState: PageStateObservable

    fun init(pageStateLayout: CompPageStateLayout) {
        pageState = PageStateObservable(pageStateLayout, PageState.LOADING)
    }

    fun loadBanner(): Observable<List<Movie>> {
        return DataRepository.instance.indexBanner()
                .compose(observableBindLifecycleWithSwitchSchedule<List<Movie>>())
    }

    fun loadData(index: Int): Observable<List<Movie>> {
        return DataRepository.instance.indexMovie(index)
                .compose(observableBindLifecycleWithSwitchSchedule())
    }

    override fun invoke(p1: PageState, p2: View) {
    }

    override fun onLoad(adapters: RefreshWithLoadMoreAdapter, pager: Pager) {
        if (pager.value == pager.defaultValue) {
            if (pageState.state != PageState.ORIGIN) {
                pageState.setPageState(PageState.LOADING)
            }

            Observable.zip(loadData(pager.value as Int),
                    loadBanner(),
                    BiFunction<List<Movie>, List<Movie>, Any> { dataResp, bannerResp -> Pair(dataResp, bannerResp) })
                    .subscribe(object : RefreshLoadMoreSubscriber<Any>(adapters, pageState) {
                        override fun testRespEmpty(resp: Any): Boolean {
                            val pair = resp as Pair<List<Movie>, List<Movie>>
                            return pair.first.isEmpty() && pair.second.isEmpty()
                        }

                        override fun doOnSuccess(resp: Any, pager: Pager) {
                            val pair = resp as Pair<List<Movie>, List<Movie>>
                            bannerItems.clear()
                            bannerItems.addAll(pair.second)
                            indexItems.clear()
                            indexItems.addAll(pair.first)

                            super.doOnSuccess(resp, pager)
                        }
                    })
        } else {
            loadData(pager.value as Int)
                    .subscribe(object : RefreshLoadMoreSubscriber<List<Movie>>(adapters) {
                        override fun testRespEmpty(resp: List<Movie>): Boolean = resp.isEmpty()

                        override fun doOnSuccess(resp: List<Movie>, pager: Pager) {
                            indexItems.addAll(resp)
                        }
                    })
        }
    }
}
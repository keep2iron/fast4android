package io.github.keep2iron.app.data

import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.util.RxTransUtil
import io.github.keep2iron.app.data.remote.ApiService
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.util.getNetworkManager
import io.reactivex.Observable

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 13:48
 */
class DataRepository private constructor() {

    companion object {
        val instance: DataRepository by lazy {
            DataRepository()
        }
    }

    fun indexMovie(index: Int): Observable<List<GsonIndex>> {
        return AbstractApplication.instance.getNetworkManager()
                .getService(ApiService::class.java)
                .indexModels(index, 10)
                .compose(RxTransUtil.rxObservableScheduler())
                .map { it.value }
    }

    fun indexBanner(): Observable<List<GsonIndex>> {
        return AbstractApplication.instance.getNetworkManager()
                .getService(ApiService::class.java)
                .indexBanner()
                .compose(RxTransUtil.rxObservableScheduler())
                .map { it.value }
    }

}

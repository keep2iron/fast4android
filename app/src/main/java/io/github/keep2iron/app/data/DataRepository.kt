package io.github.keep2iron.app.data

import io.github.keep2iron.android.ext.getComponentService
import io.github.keep2iron.android.utilities.RxTransUtil
import io.github.keep2iron.app.Application
import io.github.keep2iron.app.data.remote.ApiService
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.model.Movie
import io.github.keep2iron.pomelo.NetworkManager
import io.reactivex.Observable

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 13:48
 */
class DataRepository private constructor(private val application: Application) {

    companion object {
        val instance: DataRepository by lazy {
            DataRepository(Application.instance)
        }
    }

    fun indexMovie(index: Int): Observable<List<Movie>> {
        val networkManager = getComponentService<NetworkManager>(NetworkManager::class.java)
        return networkManager
                .getService(ApiService::class.java)
                .indexModels(index, 10)
                .compose(RxTransUtil.rxObservableScheduler())
                .map { it.value }
    }

    fun indexBanner(): Observable<List<Movie>> {
        val networkManager = getComponentService<NetworkManager>(NetworkManager::class.java)

        return networkManager
                .getService(ApiService::class.java)
                .indexBanner()
                .compose(RxTransUtil.rxObservableScheduler())
                .map { it.value }
    }

}

package io.github.keep2iron.app.data

import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.util.RxTransUtil
import io.github.keep2iron.app.entity.BaseResponse
import io.github.keep2iron.app.entity.Movie
import io.github.keep2iron.app.getApiService
import io.reactivex.Flowable

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

    fun indexMovie(): Flowable<List<Movie>> {
        return AbstractApplication.instance.getApiService()
                .indexMovie(0, 10)
                .compose(RxTransUtil.rxFlowableScheduler())
                .map { it.value }
    }

}

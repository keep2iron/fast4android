package io.github.keep2iron.app.data

import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.util.RxTransUtil
import io.github.keep2iron.app.util.getApiService
import io.github.keep2iron.app.model.GsonIndex
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

    fun indexModel(): Observable<List<GsonIndex>> {
        return AbstractApplication.instance.getApiService()
                .indexModels(0, 10)
                .compose(RxTransUtil.rxObservableScheduler())
                .map { it.value }
    }

}

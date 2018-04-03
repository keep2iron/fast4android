package io.github.keep2iron.app.ui

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.core.AbstractSubscriber
import io.github.keep2iron.android.core.LifeCycleViewModule
import io.github.keep2iron.app.data.DataRepository
import io.github.keep2iron.app.model.GsonIndex

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:31
 */
class RecommendModel(owner: LifecycleOwner) : LifeCycleViewModule(AbstractApplication.instance, owner) {
    var bannerItems: ObservableArrayList<Int> = ObservableArrayList()
    var indexItems: ObservableArrayList<GsonIndex> = ObservableArrayList()

    init {
//        bannerItems.clear()
//        bannerItems.add(R.drawable.banner01)
//        bannerItems.add(R.drawable.banner02)
    }

    fun loadData(onLoadComplete: () -> Unit = {}) {
        DataRepository.instance.indexModel()
                .compose(bindObservableLifeCycle())
                .subscribe(object : AbstractSubscriber<List<GsonIndex>>() {
                    override fun onSuccess(resp: List<GsonIndex>) {
                        indexItems.addAll(resp)
                        onLoadComplete()
                    }
                })
    }
}
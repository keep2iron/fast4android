package io.github.keep2iron.app.ui

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.ObservableArrayList
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.core.AbstractSubscriber
import io.github.keep2iron.android.core.LifeCycleViewModule
import io.github.keep2iron.app.R
import io.github.keep2iron.app.data.DataRepository
import io.github.keep2iron.app.entity.Movie

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:31
 */
class IndexModule(owner: LifecycleOwner) : LifeCycleViewModule(AbstractApplication.instance, owner) {
    var bannerItems: ObservableArrayList<Int> = ObservableArrayList()
    var videoItems: ObservableArrayList<String> = ObservableArrayList()

    fun loadData() {
        bannerItems.clear()
        bannerItems.add(R.drawable.banner01)
        bannerItems.add(R.drawable.banner02)

        videoItems.add("测试一下")
        videoItems.add("测试一下")
        videoItems.add("测试一下")
        videoItems.add("测试一下")

        DataRepository.instance.indexMovie()
                .compose(bindFlowableLifeCycle())
                .subscribe(object : AbstractSubscriber<List<Movie>>() {
                    override fun onSuccess(resp: List<Movie>) {
                        Logger.e("resp : $resp")
                    }
                })
    }
}
package io.github.keep2iron.app.ui

import android.os.Bundle
import android.view.View
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.MyWatchingFragmentBinding

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/26 11:34
 *
 * 我的观看Fragment
 */
class MyWatchingFragment : AbstractFragment<MyWatchingFragmentBinding>(), Title {
    override fun getTitle(): String = "观看"

    override val resId: Int = R.layout.my_watching_fragment

    override fun initVariables(container: View?, savedInstanceState: Bundle?) {
    }

    companion object {
        fun getInstance(): MyWatchingFragment = MyWatchingFragment()
    }

}
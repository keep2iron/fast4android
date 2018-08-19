package io.github.keep2iron.app.ui

import android.os.Bundle
import android.view.View
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.SeriesFragmentBinding

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/26 13:44
 */
class SeriesFragment : AbstractFragment<SeriesFragmentBinding>(), Title {
    override fun getTitle(): String = "系列"

    override val resId: Int = R.layout.series_fragment

    override fun initVariables(container: View?, savedInstanceState: Bundle?) {

    }

    companion object {
        fun getInstance(): SeriesFragment = SeriesFragment()
    }

    override fun lazyLoad(container: View?) {
    }
}
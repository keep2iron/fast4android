package io.github.keep2iron.app.ui

import android.os.Bundle
import android.view.View
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.FragmentMovieBinding

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/11 15:38
 */
class MovieFragment : AbstractFragment<FragmentMovieBinding>() {
    override val resId: Int = R.layout.fragment_movie

    override fun initVariables(container: View?, savedInstanceState: Bundle?) {
    }

    companion object {
        fun getInstance(): MovieFragment {
            return MovieFragment()
        }
    }
}
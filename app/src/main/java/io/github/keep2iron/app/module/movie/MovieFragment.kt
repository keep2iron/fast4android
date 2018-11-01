package io.github.keep2iron.app.module.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bana.bananasays.libaspect.FragmentVisibleDelegate
import com.orhanobut.logger.Logger
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

    var delegate: FragmentVisibleDelegate = FragmentVisibleDelegate {
        Logger.d("${this.javaClass.simpleName} show $it")
    }

    override fun initVariables(container: View, savedInstanceState: Bundle?) {
    }

    companion object {
        fun getInstance(): MovieFragment {
            return MovieFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Logger.d("onCreateView ${this.javaClass.simpleName}")
        delegate.onCreateView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
//        Logger.d("onResume ${this.javaClass.simpleName}")
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        Logger.d("${this.javaClass.simpleName} setUserVisibleHint $isVisibleToUser")
        super.setUserVisibleHint(isVisibleToUser)
        delegate.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
//        Logger.d("${this.javaClass.simpleName} onHiddenChanged $hidden")
        super.onHiddenChanged(hidden)
        delegate.onHiddenChanged(hidden)
    }

}
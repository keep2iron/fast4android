package io.github.keep2iron.app.ui

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bana.bananasays.libaspect.FragmentVisibleDelegate
import com.orhanobut.logger.Logger
import io.github.keep2iron.app.databinding.FragmentColorBinding
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.app.R

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/07 10:37
 */
class ColorFragment : AbstractFragment<FragmentColorBinding>() {
    override val resId: Int = R.layout.fragment_color

    var container: FrameLayout by FindViewById(R.id.container)

    var colorRes: Int = -1

    var delegate: FragmentVisibleDelegate = FragmentVisibleDelegate(this) {
        Logger.d("${this.javaClass.simpleName} show $it")
    }

    override fun initVariables(container: View, savedInstanceState: Bundle?) {
        dataBinding.container.setBackgroundResource(colorRes)

        dataBinding.tvRoute.setOnClickListener {
            val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
            beginTransaction.replace(R.id.container, ColorFragment.getInstance(R.color.colorPrimary))
            beginTransaction.addToBackStack(null)
            beginTransaction.commit()
        }

        this.container.setBackgroundColor(Color.RED)
    }

    companion object {
        fun getInstance(@ColorRes colorRes: Int): ColorFragment {
            val fragment = ColorFragment()
            fragment.colorRes = colorRes
            return fragment
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
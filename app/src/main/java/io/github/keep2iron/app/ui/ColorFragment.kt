package io.github.keep2iron.app.ui

import android.os.Bundle
import android.support.annotation.ColorRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import io.github.keep2iron.app.databinding.FragmentColorBinding
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.R

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/07 10:37
 */
class ColorFragment : AbstractFragment<FragmentColorBinding>() {
    override val resId: Int = R.layout.fragment_color

    var colorRes: Int = -1

    override fun initVariables(container: View?, savedInstanceState: Bundle?) {
        dataBinding.container.setBackgroundResource(colorRes)

        dataBinding.tvRoute.setOnClickListener {
            val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
            beginTransaction.replace(R.id.container, ColorFragment.getInstance(R.color.deep_gray))
            beginTransaction.addToBackStack(null)
            beginTransaction.commit()
        }
    }

    companion object {
        fun getInstance(@ColorRes colorRes: Int): ColorFragment {
            val fragment = ColorFragment()
            fragment.colorRes = colorRes
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d("${this::class.simpleName} onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        Logger.d("${this::class.simpleName} onDestroyView")
        super.onDestroyView()
    }

    override fun onDetach() {
        Logger.d("${this::class.simpleName} onDetach")
        super.onDetach()
    }
}
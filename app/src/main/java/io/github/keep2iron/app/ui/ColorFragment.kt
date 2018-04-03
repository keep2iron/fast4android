package io.github.keep2iron.app.ui

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
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

    override fun initVariables(container: View?) {
        dataBinding.container.setBackgroundColor(ContextCompat.getColor(applicationContext, colorRes))
    }

    companion object {
        fun getInstance(@ColorRes colorRes: Int): ColorFragment {
            val fragment = ColorFragment()
            fragment.colorRes = colorRes
            return fragment
        }
    }
}
package io.github.keep2iron.fast4android.app.ui.bottomnav

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.github.keep2iron.bottomnavlayout.BottomTabAdapter
import io.github.keep2iron.bottomnavlayout.BottomTabLayout
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.AbstractFragment
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate

class BottomNavFragment : AbstractFragment<ViewDataBinding>() {

    override fun resId(): Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = FrameLayout(requireContext()).apply {
            addView(
                    TextView(requireContext()).apply {
                        layoutParams =
                                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.MATCH_PARENT)
                        gravity = Gravity.CENTER
                        text = arguments!!.getString("tab")
                        textSize = 25f
                    }
            )
        }
        return contentView
    }

    override fun initVariables(savedInstanceState: Bundle?) {
    }
}

class BottomNavLayoutActivity : AbstractActivity<ViewDataBinding>() {

    private val bottomNavLayout by findViewByDelegate<BottomTabLayout>(R.id.bottomNavLayout)

    private val container by findViewByDelegate<FrameLayout>(R.id.container)

    override fun resId(): Int = R.layout.bottom_nav_activity

    override fun initVariables(savedInstanceState: Bundle?) {
        bottomNavLayout.setBottomTabAdapter(BottomTabAdapter(
                supportFragmentManager,
                listOf(
                        BottomTabAdapter.Tab(applicationContext) {
                            colorRes = R.color.fast_config_color_gray_4
                            selectColorRes = R.color.fast_config_color_blue
                            iconRes = R.mipmap.icon_tabbar_component
                            iconSelRes = R.mipmap.icon_tabbar_component_selected
                            title = "tab1"
                            iconResTintColorRes = R.color.fast_config_color_gray_4
                            iconSelResTintColorRes = R.color.fast_config_color_blue
                            fragment = Fragment.instantiate(applicationContext, BottomNavFragment::class.java.name, Bundle().apply {
                                putString("tab", "tab1")
                            })
                        },
                        BottomTabAdapter.Tab(applicationContext) {
                            colorRes = R.color.fast_config_color_gray_4
                            selectColorRes = R.color.fast_config_color_blue
                            iconRes = R.mipmap.icon_tabbar_lab
                            iconSelRes = R.mipmap.icon_tabbar_lab_selected
                            title = "tab2"
                            iconResTintColorRes = R.color.fast_config_color_gray_4
                            iconSelResTintColorRes = R.color.fast_config_color_blue
                            fragment = Fragment.instantiate(applicationContext, BottomNavFragment::class.java.name, Bundle().apply {
                                putString("tab", "tab2")
                            })
                        },
                        BottomTabAdapter.Tab(applicationContext) {
                            colorRes = R.color.fast_config_color_gray_4
                            selectColorRes = R.color.fast_config_color_blue
                            iconRes = R.mipmap.icon_tabbar_util
                            iconSelRes = R.mipmap.icon_tabbar_util_selected
                            title = "tab3"
                            iconResTintColorRes = R.color.fast_config_color_gray_4
                            iconSelResTintColorRes = R.color.fast_config_color_blue
                            fragment = Fragment.instantiate(applicationContext, BottomNavFragment::class.java.name, Bundle().apply {
                                putString("tab", "tab3")
                            })
                        }
                )
        ), container)
    }

}
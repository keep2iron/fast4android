package io.github.keep2iron.fast4android.app.ui.bottomnav

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.github.keep2iron.bottomnavlayout.BottomTabAdapter
import io.github.keep2iron.bottomnavlayout.FastBottomTabLayout
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.AbstractFragment
import io.github.keep2iron.fast4android.arch.util.LazyLoader
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.base.FastLogger
import io.github.keep2iron.fast4android.base.util.translucent

class BottomNavFragment : AbstractFragment<ViewDataBinding>() {

  override fun resId(): Int = R.layout.bottom_nav_fragment

  override fun initVariables(savedInstanceState: Bundle?) {
    val titleBar = findViewById<ViewGroup>(R.id.titleBar)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
      titleBar.requestApplyInsets()
    }

    findViewById<TextView>(R.id.tvBottomNavContent).text = arguments!!.getString("tab")

    LazyLoader.attach(this){
      val tabString = requireArguments().getString("tab", "")
      FastLogger.d("tag", "LazyLoader $tabString")
    }
  }

}

class BottomNavLayoutActivity : AbstractActivity<ViewDataBinding>() {

  private val bottomNavLayout by findViewByDelegate<FastBottomTabLayout>(R.id.bottomNavLayout)

  private val container by findViewByDelegate<View>(R.id.container)

  override fun resId(): Int = R.layout.bottom_nav_activity

  override fun initVariables(savedInstanceState: Bundle?) {
    translucent()

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
        },
        BottomTabAdapter.Tab(applicationContext) {
          colorRes = R.color.fast_config_color_gray_4
          selectColorRes = R.color.fast_config_color_blue
          iconRes = R.mipmap.icon_tabbar_util
          iconSelRes = R.mipmap.icon_tabbar_util_selected
          title = "tab4"
          iconResTintColorRes = R.color.fast_config_color_gray_4
          iconSelResTintColorRes = R.color.fast_config_color_blue
          fragment = Fragment.instantiate(applicationContext, BottomNavFragment::class.java.name, Bundle().apply {
            putString("tab", "tab4")
          })
        }
      )
    ), container)
  }

}
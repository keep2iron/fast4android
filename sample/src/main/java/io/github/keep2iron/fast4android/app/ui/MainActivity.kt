package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
import android.view.View
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.ActivityMainBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.util.LazyLoader
import io.github.keep2iron.fast4android.base.FastLogger
import io.github.keep2iron.fast4android.base.util.FastStatusBarHelper

class MainActivity : AbstractActivity<ActivityMainBinding>() {
  override fun resId(): Int = R.layout.activity_main

  override fun initVariables(savedInstanceState: Bundle?) {
    FastStatusBarHelper.translucent(this)

    LazyLoader.attach(this) {
      FastLogger.d("tag", "LazyLoader ")
    }

    showSystemUI()

    supportFragmentManager.beginTransaction()
      .replace(R.id.container, HomeFragment())
      .commit()
  }

  override fun onResume() {
    super.onResume()
    showSystemUI()
  }

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    showSystemUI()
  }


  private fun showSystemUI() {
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
  }

}
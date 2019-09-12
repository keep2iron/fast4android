package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.ActivityMainBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.base.util.FastStatusBarHelper

class MainActivity : AbstractActivity<ActivityMainBinding>() {

  override fun resId(): Int = R.layout.activity_main

  override fun initVariables(savedInstanceState: Bundle?) {
    FastStatusBarHelper.translucent(this)

    supportFragmentManager.beginTransaction()
      .replace(R.id.container, HomeFragment())
      .commit()
  }
}
package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
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

    supportFragmentManager.beginTransaction()
      .replace(R.id.container, HomeFragment())
      .commit()
  }
}
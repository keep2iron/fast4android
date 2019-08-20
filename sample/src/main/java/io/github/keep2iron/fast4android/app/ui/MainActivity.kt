package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.ActivityMainBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity

class MainActivity : AbstractActivity<ActivityMainBinding>() {

  override fun resId(): Int = R.layout.activity_main

  override fun initVariables(savedInstanceState: Bundle?) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.container, HomeFragment())
      .commit()
  }
}
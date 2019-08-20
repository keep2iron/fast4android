package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
import io.github.keep2iron.fast4android.app.R.layout
import io.github.keep2iron.fast4android.app.databinding.ActivityMainBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity

class MainActivity : AbstractActivity<ActivityMainBinding>() {

  override fun resId(): Int = layout.activity_main

  override fun initVariables(savedInstanceState: Bundle?) {
  }
}
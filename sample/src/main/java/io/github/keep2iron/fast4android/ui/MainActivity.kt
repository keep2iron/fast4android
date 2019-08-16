package io.github.keep2iron.fast4android.ui

import android.os.Bundle
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.fast4android.core.AbstractActivity
import io.github.keep2iron.fast4android.databinding.ActivityMainBinding

class MainActivity : AbstractActivity<ActivityMainBinding>() {

  override fun resId(): Int = R.layout.activity_main

  override fun initVariables(savedInstanceState: Bundle?) {
  }
}
package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity

class TabSegmentActivity : AbstractActivity<ViewDataBinding>() {

  override fun resId(): Int = R.layout.activity_main

  override fun initVariables(savedInstanceState: Bundle?) {
  }
}
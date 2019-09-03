package io.github.keep2iron.fast4android.app.ui.grouplistview

import android.os.Bundle
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.GroupListViewActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.core.util.FastStatusBarHelper

@ParallaxBack
class GroupListViewActivity : AbstractActivity<GroupListViewActivityBinding>() {

  override fun resId(): Int = R.layout.group_list_view_activity

  override fun initVariables(savedInstanceState: Bundle?) {
    FastStatusBarHelper.translucent(this)

    dataBinding.topBarLayout.setup {
      addLeftBackImageButton().setOnClickListener {
        finish()
      }
    }
  }

}
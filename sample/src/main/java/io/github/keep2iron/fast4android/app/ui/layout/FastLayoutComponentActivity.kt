package io.github.keep2iron.fast4android.app.ui.layout

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.core.alpha.FastAlphaFrameLayout

class FastLayoutComponentActivity : AbstractActivity<ViewDataBinding>() {

  val frameLayout by findViewByDelegate<FastAlphaFrameLayout>(R.id.frameLayout)

  override fun resId(): Int = R.layout.layout_comp_activity

  override fun initVariables(savedInstanceState: Bundle?) {
    frameLayout.setRadiusAndShadow(dp2px(this, 10), dp2px(this, 10), Color.BLACK, 1f)
  }
}
package io.github.keep2iron.fast4android.app.ui.detail

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import com.github.anzewei.parallaxbacklayout.ParallaxBack

@ParallaxBack
class DetailActivity : AbstractActivity<ViewDataBinding>() {

  override fun resId(): Int = R.layout.activity_detail

  override fun initVariables(savedInstanceState: Bundle?) {
  }
}
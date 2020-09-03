package io.github.keep2iron.fast4android.app.ui.density

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/3.
 *版本号：1.0

 */
@ParallaxBack
class DensityActivity : AbstractActivity<ViewDataBinding>() {
  override fun resId(): Int {
    return R.layout.activity_density
  }

  override fun initVariables(savedInstanceState: Bundle?) {

  }
}
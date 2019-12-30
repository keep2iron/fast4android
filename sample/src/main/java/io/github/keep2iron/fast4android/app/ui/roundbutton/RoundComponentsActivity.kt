package io.github.keep2iron.fast4android.app.ui.roundbutton

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.ViewCompat
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.RoundComponentsActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.base.util.FastStatusBarHelper
import io.github.keep2iron.base.util.setPaddingBottom

@ParallaxBack
class RoundComponentsActivity : AbstractActivity<RoundComponentsActivityBinding>(),
        OnClickListener {

    override fun resId(): Int = R.layout.round_components_activity

    override fun initVariables(savedInstanceState: Bundle?) {
        FastStatusBarHelper.translucent(this)

        ViewCompat.setOnApplyWindowInsetsListener(dataBinding.llContainer){v, insets ->
            dataBinding.llContainer.setPaddingBottom(insets.systemWindowInsetBottom)
            insets
        }

//        dataBinding.titleBar.fastTopBar.addLeftBackImageButton().setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fast_topbar_item_left_back -> {
                finish()
            }
        }
    }
}
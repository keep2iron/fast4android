package io.github.keep2iron.android.core

import android.databinding.ViewDataBinding
import com.github.anzewei.parallaxbacklayout.ParallaxBack

/**
 * @author keep2iron
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT,layout = ParallaxBack.Layout.PARALLAX)
abstract class AbstractSwipeBackActivity<DB : ViewDataBinding> : AbstractActivity<DB>() {
}

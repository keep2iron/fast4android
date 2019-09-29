package io.github.keep2iron.fast4android.app.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.base.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.AbstractDialogFragment
import io.github.keep2iron.fast4android.arch.FindViewById
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.core.alpha.FastAlphaRoundButton
import io.github.keep2iron.fast4android.core.util.dp2px
import io.github.keep2iron.grouplistview.FastGroupListItemView
import io.github.keep2iron.grouplistview.FastSwitchView
import io.github.keep2iron.peach.DrawableCreator

@ParallaxBack
class DialogComponentsActivity : AbstractActivity<ViewDataBinding>() {
    private val groupListCancelable: FastGroupListItemView by FindViewById(R.id.groupListCancelable)
    private val btnStartDialog: FastAlphaRoundButton by FindViewById(R.id.btnStartDialog)

    override fun resId(): Int = R.layout.activity_dialog_component
    private var cancelable: Boolean = false

    override fun initVariables(savedInstanceState: Bundle?) {
        FastStatusBarHelper.translucent(this)
        groupListCancelable.setupSwitch {
            setOnStateChangedListener(object : FastSwitchView.OnStateChangedListener {
                override fun stateChange(view: FastSwitchView, isChecked: Boolean) {
                    cancelable = isChecked
                    view.toggleSwitch(isChecked)
                }
            })
        }
        btnStartDialog.setOnClickListener {
            InnerDialog().apply {
                isCancelable = cancelable
                show(supportFragmentManager, InnerDialog::class.java.name)
            }
        }
    }

    class InnerDialog : AbstractDialogFragment<ViewDataBinding>() {
        override fun resId(): Int = R.layout.dialog_fragment_inner

        override fun initVariables(container: View) {
            container.findViewById<View>(R.id.fastRoundImageButton).setOnClickListener {
                dismissAllowingStateLoss()
            }
            container.background = DrawableCreator()
                    .cornerRadius(10)
                    .solidColor(ContextCompat.getColor(requireContext(), R.color.fast_config_color_white))
                    .build()
        }

        override fun width(): Int {
            return dp2px(300)
        }

        override fun gravity(): Int = Gravity.CENTER
    }
}
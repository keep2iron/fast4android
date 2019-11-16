package io.github.keep2iron.fast4android.app.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.base.util.translucent
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.AbstractDialogFragment
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.core.alpha.FastAlphaRoundTextView
import io.github.keep2iron.fast4android.core.util.dp2px
import io.github.keep2iron.fast4android.core.widget.FastDialogAction
import io.github.keep2iron.fast4android.core.widget.MessageDialogBuilder
import io.github.keep2iron.peach.DrawableCreator

@ParallaxBack
class DialogComponentsActivity : AbstractActivity<ViewDataBinding>() {
    //    private val groupListCancelable: FastGroupListItemView by findViewByDelegate(R.id.groupListCancelable)
    private val btnStartDialogFragment: FastAlphaRoundTextView by findViewByDelegate(R.id.btnStartDialogFragment)
    private val btnStartMessageDialog: FastAlphaRoundTextView by findViewByDelegate(R.id.btnStartMessageDialog)

    override fun resId(): Int = R.layout.activity_dialog_component
    private var cancelable: Boolean = false

    override fun initVariables(savedInstanceState: Bundle?) {
        translucent()

//        groupListCancelable.setupSwitch {
//            setOnStateChangedListener(object : FastSwitchView.OnStateChangedListener {
//                override fun stateChange(view: FastSwitchView, isChecked: Boolean) {
//                    cancelable = isChecked
//                    view.toggleSwitch(isChecked)
//                }
//            })
//        }
        btnStartDialogFragment.setOnClickListener {
            InnerDialog().apply {
                isCancelable = cancelable
                show(supportFragmentManager, InnerDialog::class.java.name)
            }
        }
        btnStartMessageDialog.setOnClickListener {
            MessageDialogBuilder(this) {
                title = "标题"
                message = "确定要发送吗？"
                backgroundRadius = dp2px(10)
                addAction {
                    content = "取消"
                    actionProp = FastDialogAction.ACTION_PROP_NEGATIVE
                }
                addAction {
                    content = "确认"
                    actionProp = FastDialogAction.ACTION_PROP_POSITIVE
                }
            }.build().show()
        }
    }

    class InnerDialog : AbstractDialogFragment<ViewDataBinding>() {
        override fun resId(): Int = R.layout.dialog_fragment_inner

        override fun initVariables(container: View) {
            container.findViewById<View>(R.id.fastRoundImageButton).setOnClickListener {
                dismissAllowingStateLoss()
            }
            container.background = DrawableCreator()
                    .cornerRadius(0)
                    .solidColor(ContextCompat.getColor(requireContext(), R.color.fast_config_color_white))
                    .build()
        }

        override fun width(): Int {
            return dp2px(300)
        }

        override fun gravity(): Int = Gravity.CENTER
    }
}
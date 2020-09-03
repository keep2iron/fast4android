package io.github.keep2iron.fast4android.app.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.AbstractDialogFragment
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.base.FastLogger
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.base.util.translucent
import io.github.keep2iron.fast4android.core.alpha.FastAlphaRoundTextView
import io.github.keep2iron.fast4android.core.widget.FastDialogAction
import io.github.keep2iron.fast4android.core.widget.MessageDialogBuilder
import io.github.keep2iron.peach.DrawableCreator

@ParallaxBack
class DialogComponentsActivity : AbstractActivity<ViewDataBinding>() {
  //    private val groupListCancelable: FastGroupListItemView by findViewByDelegate(R.id.groupListCancelable)
  private val btnStartDialogFragment: FastAlphaRoundTextView by findViewByDelegate(R.id.btnStartDialogFragment)
  private val btnStartMessageDialog: FastAlphaRoundTextView by findViewByDelegate(R.id.btnStartMessageDialog)
  private val btnStartMessageDialog2: FastAlphaRoundTextView by findViewByDelegate(R.id.btnStartMessageDialog2)

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
        backgroundRadius = dp2px(this@DialogComponentsActivity, 10)
        addAction {
          content = "取消"
          actionProp = FastDialogAction.ACTION_PROP_NEUTRAL
        }
        addAction {
          content = "确认"
          actionProp = FastDialogAction.ACTION_PROP_POSITIVE
        }
      }.build().show()
    }
    btnStartMessageDialog2.setOnClickListener {
      MessageDialogBuilder(this) {
        title = "标题"
        message = "确定要发送吗？"
        backgroundRadius = dp2px(this@DialogComponentsActivity, 10)
        addAction {
          content = "取消"
          actionProp = FastDialogAction.ACTION_PROP_NEUTRAL
        }
        addAction {
          content = "删除"
          actionProp = FastDialogAction.ACTION_PROP_NEGATIVE
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

    override fun width(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    override fun height(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun gravity(): Int = Gravity.BOTTOM
  }

  override fun onResume() {
    super.onResume()
    FastLogger.d("test", "onResume")
  }
}
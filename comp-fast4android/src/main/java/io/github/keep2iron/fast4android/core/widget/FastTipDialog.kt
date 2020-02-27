package io.github.keep2iron.fast4android.core.widget

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px

enum class Tip {
  NOTHING,
  LOADING,
  SUCCESSFUL,
  FAIL,
  INFO
}

class FastTipDialogBuilder(val context: Context,
                           block: FastTipDialogBuilder.() -> Unit) {

  var themeResId: Int = R.style.Fast_TipDialog

  var iconType = Tip.NOTHING

  var tipContent: String = ""

  init {
    this.block()
  }


  fun build(cancelable: Boolean = false): FastDialog {
    val dialog = FastDialog(context, themeResId)
    dialog.setCancelable(cancelable)
    dialog.setContentView(R.layout.fast_tip_dialog_layout)

    val contentWrap = dialog.findViewById<LinearLayout>(R.id.contentWrap)

    when (iconType) {
      Tip.NOTHING -> {

      }
      Tip.LOADING -> {
        val loadingView = FastLoadingView(dialog.context, dp2px(context, 32), Color.WHITE)
        val loadingViewLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        loadingView.layoutParams = loadingViewLP
        contentWrap.addView(loadingView)
      }
      Tip.SUCCESSFUL, Tip.FAIL, Tip.INFO -> {
        val imageView = ImageView(dialog.context)
        val imageViewLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        imageView.layoutParams = imageViewLP

        when (iconType) {
          Tip.SUCCESSFUL -> {
            imageView.setImageDrawable(ContextCompat.getDrawable(dialog.context, R.drawable.fast_icon_notify_done))
          }
          Tip.FAIL -> {
            imageView.setImageDrawable(ContextCompat.getDrawable(dialog.context, R.drawable.fast_icon_notify_error))
          }
          Tip.INFO -> {
            imageView.setImageDrawable(ContextCompat.getDrawable(dialog.context, R.drawable.fast_icon_notify_info))
          }
          else -> {
            throw IllegalArgumentException("iconType $iconType is Illegal.")
          }
        }

        contentWrap.addView(imageView)
      }
    }

    if (tipContent.isNotEmpty()) {
      val tipView = TextView(dialog.context)
      val tipViewLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
      if (iconType != Tip.NOTHING) {
        tipViewLP.topMargin = dp2px(context, 12)
      }
      tipView.layoutParams = tipViewLP
      tipView.ellipsize = TextUtils.TruncateAt.END
      tipView.gravity = Gravity.CENTER
      tipView.maxLines = 2
      tipView.setTextColor(ContextCompat.getColor(dialog.context, R.color.fast_config_color_white))
      tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
      tipView.text = tipContent
      contentWrap.addView(tipView)
    }

    return dialog
  }

}
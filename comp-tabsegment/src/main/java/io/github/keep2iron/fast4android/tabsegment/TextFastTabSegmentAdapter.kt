package io.github.keep2iron.fast4android.tabsegment

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import io.github.keep2iron.fast4android.core.util.dp2px
import io.github.keep2iron.fast4android.core.util.setPaddingLeft
import io.github.keep2iron.fast4android.core.util.setPaddingRight

open class TextFastTabSegmentAdapter(private val data: List<String>) : TabSegmentAdapter() {
  override fun onBindTab(view: View, index: Int, selected: Boolean) {
    val textView = (view as TextView)
    textView.text = data[index]
    if (selected) {
      textView.setTextColor(selectColor)
    } else {
      textView.setTextColor(normalColor)
    }
  }

  override fun getItemSize(): Int = data.size

  override fun createTab(parentView: View, index: Int, selected: Boolean): View {
    return TextView(parentView.context).apply {
      gravity = Gravity.CENTER
      setTextColor(
        if (!selected) {
          normalColor
        } else {
          selectColor
        }
      )
      setPaddingLeft(dp2px(16))
      setPaddingRight(dp2px(16))
      text = data[index]
    }
  }

//  override fun normalColor(context: Context, index: Int): Int =
//    ContextCompat.getColor(context, R.color.fast_config_color_gray_1)
//
//  override fun selectColor(context: Context, index: Int): Int =
//    ContextCompat.getColor(context, R.color.fast_config_color_blue)
}
package io.github.keep2iron.fast4android.tabsegment

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.base.util.setPaddingLeft
import io.github.keep2iron.fast4android.base.util.setPaddingRight

open class TextFastTabSegmentAdapter(private val data: List<String>) : TabSegmentAdapter() {

  override fun onTabStateChanged(view: View, index: Int, selected: Boolean) {
    val textView = (view as TextView)
    textView.text = data[index]
    if (selected) {
      textView.setTextColor(selectColor)
    } else {
      textView.setTextColor(normalColor)
    }
  }

  override fun getItemSize(): Int = data.size

  override fun createTab(parentView: ViewGroup, index: Int, selected: Boolean): View {
    return TextView(parentView.context).apply {
      gravity = Gravity.CENTER
      setTextColor(
        if (!selected) {
          normalColor
        } else {
          selectColor
        }
      )
      setPaddingLeft(dp2px(context, 16))
      setPaddingRight(dp2px(context, 16))
      text = data[index]
    }
  }

}
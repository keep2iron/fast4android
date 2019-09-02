package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractFragment

class TabSegmentFragment : AbstractFragment<ViewDataBinding>() {

  override fun resId(): Int = R.layout.tab_segment_item_fargment

  override fun initVariables(savedInstanceState: Bundle?) {
//    contentView.setBackgroundColor(arguments!!.getInt("color"))
  }

  companion object {
    fun newInstance(color: Int): TabSegmentFragment {
      return TabSegmentFragment().apply {
        arguments = Bundle()
          .apply {
            putInt("color", color)
          }
      }
    }
  }
}
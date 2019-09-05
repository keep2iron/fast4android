package io.github.keep2iron.grouplistview

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import io.github.keep2iron.fast4android.core.util.dp2px
import io.github.keep2iron.fast4android.core.util.getAttrColor
import io.github.keep2iron.fast4android.core.util.getAttrDimen
import io.github.keep2iron.grouplistview.FastCommonListItemView.AccessoryType

class FastGroupListContainer @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

  companion object {
    const val SEPARATOR_STYLE_NONE = 1

    const val SEPARATOR_STYLE_NORMAL = 0
  }

  var separatorStyle = SEPARATOR_STYLE_NORMAL
    set(value) {
      field = value
      if (field == SEPARATOR_STYLE_NONE) {
        dividerDrawable = null
      }
    }

  private var insetRect = Rect()

  init {
    orientation = VERTICAL

    val typedArray =
      context.obtainStyledAttributes(attrs, R.styleable.FastGroupListContainer, defStyleAttr, 0)
    separatorStyle =
      typedArray.getInt(R.styleable.FastGroupListContainer_fast_separatorStyle, separatorStyle)
    typedArray.recycle()
  }

  inline fun addItem(
    @AccessoryType accessoryType: Int = FastCommonListItemView.ACCESSORY_TYPE_NONE, block: FastCommonListItemView.() -> Unit
  ) {
    val item = FastCommonListItemView(context)
    item.accessoryType = accessoryType
    item.apply(block)
    val attrHeight = context.getAttrDimen(R.attr.fast_list_item_height)
    item.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, attrHeight)
    item.setBackgroundResource(R.drawable.fast_s_list_item_bg_with_border_none)
    addView(item)
  }

  inline fun addChevronItem(block: FastCommonListItemView.() -> Unit) {
    addItem(FastCommonListItemView.ACCESSORY_TYPE_CHEVRON, block)
  }

  inline fun addSwitchItem(block: FastCommonListItemView.() -> Unit) {
    addItem(FastCommonListItemView.ACCESSORY_TYPE_SWITCH, block)
  }

  inline fun addCustomItem(block: FastCommonListItemView.() -> Unit) {
    addItem(FastCommonListItemView.ACCESSORY_TYPE_CUSTOM, block)
  }

  /**‘
   * 设置divider的内边距，当[separatorStyle]为[SEPARATOR_STYLE_NORMAL]有效
   */
  fun setInset(left: Int = -1, top: Int = -1, right: Int = -1, bottom: Int = -1) {
    if (left > 0) {
      insetRect.left = left
    }
    if (top > 0) {
      insetRect.top = left
    }
    if (right > 0) {
      insetRect.right = left
    }
    if (bottom > 0) {
      insetRect.bottom = left
    }

    showDividers = SHOW_DIVIDER_MIDDLE
    val drawable = GradientDrawable().apply {
      setSize(LayoutParams.MATCH_PARENT, dp2px(1))
      setColor(context.getAttrColor(R.attr.fast_config_color_separator))
    }

    dividerDrawable =
      if (insetRect.left > 0 || insetRect.top > 0 || insetRect.right > 0 || insetRect.bottom > 0) {
        InsetDrawable(
          drawable,
          insetRect.left,
          insetRect.top,
          insetRect.right,
          insetRect.bottom
        )
      } else {
        drawable
      }
  }

}
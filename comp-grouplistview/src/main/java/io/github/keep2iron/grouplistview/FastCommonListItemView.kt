package io.github.keep2iron.grouplistview

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import io.github.keep2iron.fast4android.core.util.getAttrColor
import kotlin.annotation.AnnotationRetention.SOURCE

class FastCommonListItemView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = R.attr.FastCommonListItemViewStyle
) : RelativeLayout(context, attrs, defStyleAttr) {

  companion object {
    /**
     * 右侧不显示任何东西
     */
    const val ACCESSORY_TYPE_NONE = 0
    /**
     * 右侧显示一个箭头
     */
    const val ACCESSORY_TYPE_CHEVRON = 1
    /**
     * 右侧显示一个开关
     */
    const val ACCESSORY_TYPE_SWITCH = 2
    /**
     * 自定义右侧显示的 View
     */
    const val ACCESSORY_TYPE_CUSTOM = 3
  }

  @IntDef(ACCESSORY_TYPE_NONE, ACCESSORY_TYPE_CHEVRON, ACCESSORY_TYPE_SWITCH, ACCESSORY_TYPE_CUSTOM)
  @Retention(SOURCE)
  annotation class AccessoryType

  @AccessoryType var accessoryType = ACCESSORY_TYPE_NONE

  var title: String = ""
  @ColorInt var titleColor: Int = context.getAttrColor(R.color.fast_config_color_gray_3)

  init {
    val typedArray =
      context.obtainStyledAttributes(attrs, R.styleable.FastCommonListItemView, defStyleAttr, 0)
    accessoryType =
      typedArray.getInt(R.styleable.FastCommonListItemView_fast_accessory_type, accessoryType)
    title = typedArray.getString(R.styleable.FastCommonListItemView_fast_title) ?: ""
    titleColor =
      typedArray.getColor(R.styleable.FastCommonListItemView_fast_commonList_titleColor, titleColor)
    typedArray.recycle()
  }

}
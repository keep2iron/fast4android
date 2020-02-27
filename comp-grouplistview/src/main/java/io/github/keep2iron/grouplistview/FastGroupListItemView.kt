package io.github.keep2iron.grouplistview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.base.util.getAttrColor
import io.github.keep2iron.fast4android.base.util.setPaddingLeft
import kotlin.annotation.AnnotationRetention.SOURCE

class FastGroupListItemView @JvmOverloads constructor(
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

  private var defaultAccessoryViewRef: View? = null
  @AccessoryType
  var accessoryType = ACCESSORY_TYPE_NONE
    set(value) {
      if (field == value) return
      field = value
      when (value) {
        ACCESSORY_TYPE_NONE -> {
          Log.d(FastGroupListItemView::class.java.simpleName, "ACCESSORY_TYPE_NONE")
          groupListItemAccessoryView.removeAllViews()
          groupListItemAccessoryView.visibility = View.GONE
        }
        ACCESSORY_TYPE_CHEVRON -> {
          Log.d(FastGroupListItemView::class.java.simpleName, "ACCESSORY_TYPE_CHEVRON")
          groupListItemAccessoryView.removeAllViews()
          val imageView = AppCompatImageView(context)
          imageView.setImageResource(R.drawable.fast_icon_chevron)
          imageView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
          ).apply {
            gravity = Gravity.CENTER_VERTICAL
          }
          defaultAccessoryViewRef = imageView
          groupListItemAccessoryView.addView(imageView)
          groupListItemAccessoryView.visibility = View.VISIBLE
        }
        ACCESSORY_TYPE_SWITCH -> {
          Log.d(FastGroupListItemView::class.java.simpleName, "ACCESSORY_TYPE_SWITCH")
          groupListItemAccessoryView.removeAllViews()
          val switchView = FastSwitchView(context)
          switchView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
          ).apply {
            gravity = Gravity.CENTER_VERTICAL
          }
          defaultAccessoryViewRef = switchView
          groupListItemAccessoryView.addView(switchView)
          groupListItemAccessoryView.visibility = View.VISIBLE
        }
        ACCESSORY_TYPE_CUSTOM -> {
          Log.d(FastGroupListItemView::class.java.simpleName, "ACCESSORY_TYPE_CUSTOM")
          groupListItemAccessoryView.visibility = View.VISIBLE
        }
      }
    }
  var title: String = ""
    set(value) {
      field = value
      groupListItemTextView.text = field
    }
  @ColorInt
  var titleColor: Int = context.getAttrColor(R.color.fast_config_color_gray_3)
  private var groupListItemImageView: AppCompatImageView
  private var groupListItemAccessoryView: FrameLayout
  private var groupListItemTextContainer: LinearLayout
  private var groupListItemTextView: TextView
  private var groupListItemTipsDot: ImageView

  init {
    LayoutInflater.from(context).inflate(R.layout.fast_widget_group_list_item_view, this, true)

    groupListItemImageView = findViewById(R.id.groupListItemImageView)
    groupListItemAccessoryView = findViewById(R.id.groupListItemAccessoryView)
    groupListItemTextContainer = findViewById(R.id.groupListItemTextContainer)
    groupListItemTextView = findViewById(R.id.groupListItemTextView)
    groupListItemTipsDot = findViewById(R.id.groupListItemTipsDot)
    val typedArray =
      context.obtainStyledAttributes(attrs, R.styleable.FastGroupListItemView, defStyleAttr, 0)
    accessoryType =
      typedArray.getInt(R.styleable.FastGroupListItemView_fast_accessory_type, accessoryType)
    title = typedArray.getString(R.styleable.FastGroupListItemView_fast_title) ?: ""
    titleColor =
      typedArray.getColor(R.styleable.FastGroupListItemView_fast_commonList_titleColor, titleColor)
    typedArray.recycle()

    setTitleDrawablePadding(dp2px(context, 8))
  }

  fun setLeftGroupListImageDrawable(drawable: Drawable?) {
    groupListItemImageView.setImageDrawable(drawable)
    if (drawable != null) {
      groupListItemImageView.visibility = View.VISIBLE
    } else {
      groupListItemImageView.visibility = View.GONE
    }
  }

  fun setLeftGroupListImageResource(@DrawableRes resId: Int) {
    groupListItemImageView.setImageResource(resId)

    groupListItemImageView.visibility = View.VISIBLE
  }

  fun setLeftImageSize(width: Int, height: Int) {
    groupListItemImageView.layoutParams =
      LayoutParams(groupListItemImageView.layoutParams).apply {
        this.width = width
        this.height = height
        this.addRule(CENTER_VERTICAL)
      }
    groupListItemImageView.requestLayout()
  }

  fun setTitleDrawablePadding(padding: Int) {
    groupListItemTextView.setPaddingLeft(paddingLeft)
  }

  fun addAccessoryCustomView(view: View) {
    groupListItemAccessoryView.addView(view)
  }

  fun getAccessoryView(@IdRes id: Int = -1): View {
    return if (id != -1) {
      groupListItemAccessoryView.findViewById<View>(id)
    } else {
      groupListItemAccessoryView.getChildAt(0)
    }
  }

  fun setupSwitch(block: FastSwitchView.() -> Unit) {
    if (accessoryType == ACCESSORY_TYPE_SWITCH) {
      (defaultAccessoryViewRef as FastSwitchView).apply(block)
    }
  }
}
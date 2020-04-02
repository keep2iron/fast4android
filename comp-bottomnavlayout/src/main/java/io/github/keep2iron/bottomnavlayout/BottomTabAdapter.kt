package io.github.keep2iron.bottomnavlayout

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/02 11:45
 */
open class BottomTabAdapter(private val supportFragmentManager: FragmentManager, val tabs: List<Tab>) {

  val onTabStateChangedListeners: ArrayList<FastBottomTabLayout.OnTabChangeListener> = ArrayList()
  var selectPosition: Int = 0

  lateinit var showingFragment: Fragment
  lateinit var containerView: View

  fun getItemCount(): Int = tabs.size

  open fun onBindTabHolder(position: Int, tabHolder: Tab) {
  }

  internal fun provideDefaultTextView(context: Context,
                                      tab: Tab,
                                      tabTextSize: Int,
                                      tabLayoutHeight: Float,
                                      drawablePadding: Int,
                                      isSelect: Boolean = false): TextView {
    return BadgeTextView(context).apply {
      val drawable = ContextCompat.getDrawable(context, if (isSelect) tab.iconSelRes else tab.iconRes)
      if (drawable != null) {
        if (tab.iconResTintColorRes != 0) {
          DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tab.iconResTintColorRes))
        }
        drawable.setBounds(0, 0, tab.tabIconWidth.toInt(), tab.tabIconHeight.toInt())
        setCompoundDrawables(null, drawable, null, null)
      }

      gravity = Gravity.CENTER
      val tabIconTopPadding = if (!TextUtils.isEmpty(tab.title)) {
        text = tab.title
        setTextColor(ContextCompat.getColor(context, if (isSelect) tab.selectColorRes else tab.colorRes))
        (tabLayoutHeight - tabTextSize - tab.tabIconHeight - drawablePadding) / 2
      } else {
        text = null
        (tabLayoutHeight - tab.tabIconHeight - drawablePadding) / 2
      }
      compoundDrawablePadding = drawablePadding
      setPadding(0, tabIconTopPadding.toInt(), 0, 0)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize.toFloat())
      badgeColor = tab.badgeColor
      badgeSizeRes = tab.badgeSizeRes
    }
  }

  private fun onTabSelect(position: Int) {
    val tab = tabs[position]

    for (listener in onTabStateChangedListeners) {
      listener.onTabSelect(position)
    }

    if (!tab.isEnable) {
      return
    }

    tabs[selectPosition].unSelect()
    for (listener in onTabStateChangedListeners) {
      listener.onTabUnSelect(selectPosition)
    }

    val manager = supportFragmentManager
    val fragment = tab.fragment
    tab.select()
    val fragmentTransaction = manager.beginTransaction()
    fragment?.let { frag ->
      if (frag.isAdded) {
        fragmentTransaction.hide(showingFragment).show(frag)
      } else {
        fragmentTransaction.hide(showingFragment).add(containerView.id, frag, "$position").show(frag)
      }
      showingFragment = frag
    }
    fragmentTransaction.commit()

    selectPosition = position
  }

  internal fun setTabSelect(position: Int) {
    if (containerView is ViewPager2) {
      tabs[selectPosition].unSelect()
      tabs[position].select()
      (containerView as ViewPager2).currentItem = position
    } else {
      onTabSelect(position)
    }
  }

  open class Tab {
    protected val context: Context

    @ColorRes
    var colorRes: Int = 0

    @ColorRes
    var selectColorRes: Int = 0

    var title: String = ""

    @DrawableRes
    var iconRes: Int = 0

    @DrawableRes
    var iconSelRes: Int = 0

    @ColorInt
    var iconResTintColorRes: Int = 0

    @ColorInt
    var iconSelResTintColorRes: Int = 0

    @ColorRes
    var badgeColor: Int = R.color.fast_config_color_red

    var fragment: Fragment? = null

    lateinit var customView: View
      internal set

    internal var isCustom: Boolean = false

    /**
     * 默认值会从BottomTabLayout中设置
     * @see FastBottomTabLayout
     *
     */
    var tabIconWidth: Float = 0f
    var tabIconHeight: Float = 0f

    @DimenRes
    var badgeSizeRes: Int = R.dimen.fast_default_badge_size
    var isEnable = true

    constructor(context: Context, block: Tab.() -> Unit) {
      this.context = context
      block()
    }

    constructor(customView: View) {
      this.context = customView.context
      this.customView = customView
      isCustom = true
    }

    internal fun select() {
      if (!isCustom) {
        setTextViewStyle(true)
      }
    }

    internal fun unSelect() {
      if (!isCustom) {
        setTextViewStyle(false)
      }
    }

    private fun setTextViewStyle(isSelect: Boolean) {
      (customView as TextView).apply {
        val drawable = ContextCompat.getDrawable(context, if (isSelect) iconSelRes else iconRes)
        if (drawable != null) {
          val tintColorRes = if (isSelect) iconSelResTintColorRes else iconResTintColorRes
          if (tintColorRes != 0) {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tintColorRes))
          }
          drawable.setBounds(0, 0, tabIconWidth.toInt(), tabIconHeight.toInt())
          setCompoundDrawables(null, drawable, null, null)
        }
        if (!TextUtils.isEmpty(title)) {
          setTextColor(ContextCompat.getColor(context, if (isSelect) selectColorRes else colorRes))
        }
      }
    }
  }
}
package io.github.keep2iron.android.widget

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import io.github.keep2iron.android.comp.R

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/02 11:45
 */
class BottomTabAdapter(context: Context, val tabs: ArrayList<TabHolder>) {
    private val supportFragmentManager: FragmentManager = (context as FragmentActivity).supportFragmentManager

    val onTabStateChangedListeners: ArrayList<CompBottomTabLayout.OnTabChangeListener> = ArrayList()
    var selectPosition: Int = 0

    lateinit var showingFragment: Fragment
    lateinit var containerView: View

    fun getItemCount(): Int = tabs.size

    fun onBindTabHolder(position: Int, tabHolder: TabHolder) {
    }

    internal fun provideDefaultTextView(context: Context,
                                        tab: TabHolder,
                                        tabIconWidth: Float,
                                        tabIconHeight: Float,
                                        tabTextSize: Int,
                                        tabLayoutHeight: Float,
                                        drawablePadding: Int,
                                        isSelect: Boolean = false): TextView {
        return CompBadgeTextView(context).apply {
            val drawable = ContextCompat.getDrawable(context, if (isSelect) tab.selIconResId else tab.iconResId)
            if (drawable != null) {
                drawable.setBounds(0, 0, tabIconWidth.toInt(), tabIconHeight.toInt())
                setCompoundDrawables(null, drawable, null, null)
            }

            gravity = Gravity.CENTER
            val tabIconTopPadding = if (!TextUtils.isEmpty(tab.title)) {
                text = tab.title
                setTextColor(ContextCompat.getColor(context, if (isSelect) tab.selectColorRes else tab.colorRes))
                (tabLayoutHeight - tabTextSize - tabIconHeight - drawablePadding) / 2
            } else {
                text = null
                (tabLayoutHeight - tabIconHeight - drawablePadding) / 2
            }
            compoundDrawablePadding = drawablePadding
            setPadding(0, tabIconTopPadding.toInt(), 0, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize.toFloat())
            badgeColor = tab.badgeColor
            badgeSize = tab.badgeSize
        }
    }

    internal fun setTabSelect(position: Int) {
        if (containerView is ViewPager) {
            tabs[selectPosition].unSelect()
            tabs[position].select()
            (containerView as ViewPager).currentItem = position
        } else {
            onTabSelect(position)
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

    class TabHolder {
        internal var colorRes: Int = 0
        internal var selectColorRes: Int = 0

        internal var title: String = ""
        internal var iconResId: Int = 0
        internal var selIconResId: Int = 0

        @ColorRes
        internal var badgeColor: Int = 0

        var fragment: Fragment? = null

        lateinit var customView: View

        var isCustom: Boolean = false
        var tabIconWidth: Float = 0f
        var tabIconHeight: Float = 0f
        var badgeSize: Int = 0
        var isEnable = true

        constructor(@ColorRes colorRes: Int,
                    @ColorRes selectColorRes: Int,
                    @ColorRes badgeColor: Int = android.R.color.holo_red_light,
                    @DimenRes badgeSize: Int = R.dimen.comp_default_badge_size,
                    title: String,
                    @DrawableRes iconResId: Int,
                    @DrawableRes selIconResId: Int,
                    fragment: Fragment) {
            this.colorRes = colorRes
            this.selectColorRes = selectColorRes
            this.title = title
            this.iconResId = iconResId
            this.selIconResId = selIconResId
            this.fragment = fragment
            this.badgeColor = badgeColor
            this.badgeSize = badgeSize
            isCustom = false
        }

        constructor(@DrawableRes iconResId: Int,
                    @DrawableRes selIconResId: Int,
                    fragment: Fragment) : this(0, 0, android.R.color.holo_red_light, 0, "", iconResId, selIconResId, fragment)

        constructor(mCustomView: View) {
            isCustom = true
            this.customView = mCustomView
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
                val drawable = ContextCompat.getDrawable(context, if (isSelect) selIconResId else iconResId)
                if (drawable != null) {
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
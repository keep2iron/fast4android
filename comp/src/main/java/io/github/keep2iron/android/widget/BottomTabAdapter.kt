package io.github.keep2iron.android.widget

import android.content.Context
import android.support.annotation.ColorRes
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
import com.scwang.smartrefresh.layout.util.DensityUtil.dp2px

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/02 11:45
 */
class BottomTabAdapter(context: Context, val tabs: ArrayList<TabHolder>) {
    private val supportFragmentManager: FragmentManager = (context as FragmentActivity).supportFragmentManager

    val onTabStateChangedListeners: ArrayList<BottomTabLayout.OnTabChangeListener> = ArrayList()
    var selectPosition: Int = 0

    lateinit var showingFragment: Fragment
    lateinit var containerView: View

    fun getItemCount(): Int = tabs.size

    fun onBindTabHolder(position: Int, tabHolder: TabHolder) {
    }

    internal fun provideDefaultTextView(context: Context,
                                        tab: TabHolder,
                                        tabIconWidth: Int,
                                        tabIconHeight: Int,
                                        tabTextSize: Int,
                                        drawablePadding: Int,
                                        isSelect: Boolean = false): TextView {
        return TextView(context).apply {
            val drawable = ContextCompat.getDrawable(context, if (isSelect) tab.selIconResId else tab.iconResId)
            if (drawable != null) {
                drawable.setBounds(0, 0, tabIconWidth, tabIconHeight)
                setCompoundDrawables(null, drawable, null, null)
            }

//            setBackgroundResource(R.drawable.selector_item_touch)
            gravity = Gravity.CENTER
            if(!TextUtils.isEmpty(tab.title)) {
                text = tab.title
                setTextColor(ContextCompat.getColor(context, if (isSelect) tab.selectColorRes else tab.colorRes))
            }
            compoundDrawablePadding = drawablePadding
            setPadding(0, dp2px(10f), 0, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize.toFloat())
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
        if (fragment!!.isAdded) {
            fragmentTransaction.hide(showingFragment).show(fragment)
        } else {
            fragmentTransaction.hide(showingFragment).add(containerView.id, fragment, "$position").show(fragment)
        }
        showingFragment = fragment
        fragmentTransaction.commit()

        selectPosition = position
    }

    class TabHolder {
        internal var colorRes: Int = 0
        internal var selectColorRes: Int = 0

        internal var title: String = ""
        internal var iconResId: Int = 0
        internal var selIconResId: Int = 0

        var fragment: Fragment? = null

        lateinit var customView: View

        var isCustom: Boolean = false
        var tabIconWidth: Int = 0
        var tabIconHeight: Int = 0

        var isEnable = true

        constructor(@ColorRes colorRes: Int,
                    @ColorRes selectColorRes: Int,
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

            isCustom = false
        }

        constructor(@DrawableRes iconResId: Int,
                    @DrawableRes selIconResId: Int,
                    fragment: Fragment) : this(0, 0, "", iconResId, selIconResId, fragment)

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
                    drawable.setBounds(0, 0, tabIconWidth, tabIconHeight)
                    setCompoundDrawables(null, drawable, null, null)
                }
                if (!TextUtils.isEmpty(title)) {
                    setTextColor(ContextCompat.getColor(context, if (isSelect) selectColorRes else colorRes))
                }
            }
        }
    }
}
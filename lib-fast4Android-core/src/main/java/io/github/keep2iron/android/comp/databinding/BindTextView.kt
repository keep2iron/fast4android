package io.github.keep2iron.android.comp.databinding

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.widget.TextView

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/20 11:44
 */
object BindTextView {

    @BindingAdapter("android:drawableLeft", "drawableLeftWidth", "drawableLeftHeight")
    fun drawableLeft(textView: TextView,
                     drawableId: Int,
                     drawableWidth: Float,
                     drawableHeight: Float) {
        val compoundDrawables = textView.compoundDrawables
        val context = textView.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        textView.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }

    @BindingAdapter("android:drawableTop", "drawableTopWidth", "drawableTopHeight")
    fun drawableTop(textView: TextView,
                    drawableId: Int,
                    drawableWidth: Float,
                    drawableHeight: Float) {
        val compoundDrawables = textView.compoundDrawables
        val context = textView.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        textView.setCompoundDrawables(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
    }

    @BindingAdapter(value = *arrayOf("android:drawableRight", "drawableRightWidth", "drawableRightHeight"))
    fun drawableRight(textView: TextView,
                      drawableId: Int,
                      drawableWidth: Float,
                      drawableHeight: Float) {
        val compoundDrawables = textView.compoundDrawables
        val context = textView.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    }

    @BindingAdapter(value = *arrayOf("android:drawableBottom", "drawableBottomWidth", "drawableBottomHeight"))
    fun drawableBottom(textView: TextView,
                       drawableId: Int,
                       drawableWidth: Float,
                       drawableHeight: Float) {
        val compoundDrawables = textView.compoundDrawables
        val context = textView.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    }
}
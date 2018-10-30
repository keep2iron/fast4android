package io.github.keep2iron.android.databinding

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.widget.EditText

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/29 11:42
 */
object BindEditText {

    @BindingAdapter("android:drawableLeft", "drawableLeftWidth", "drawableLeftHeight")
    fun drawableLeft(editText: EditText,
                     drawableId: Int,
                     drawableWidth: Float,
                     drawableHeight: Float) {
        val compoundDrawables = editText.compoundDrawables
        val context = editText.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        editText.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }

    @BindingAdapter("android:drawableTop", "drawableTopWidth", "drawableTopHeight")
    fun drawableTop(editText: EditText,
                    drawableId: Int,
                    drawableWidth: Float,
                    drawableHeight: Float) {
        val context = editText.context.applicationContext
        val compoundDrawables = editText.compoundDrawables
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        editText.setCompoundDrawables(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
    }

    @BindingAdapter("android:drawableRight", "drawableRightWidth", "drawableRightHeight")
    fun drawableRight(editText: EditText,
                      drawableId: Int,
                      drawableWidth: Float,
                      drawableHeight: Float) {
        val compoundDrawables = editText.compoundDrawables

        val context = editText.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        editText.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    }

    @BindingAdapter("android:drawableBottom", "drawableBottomWidth", "drawableBottomHeight")
    fun drawableBottom(editText: EditText,
                       drawableId: Int,
                       drawableWidth: Float,
                       drawableHeight: Float) {
        val compoundDrawables = editText.compoundDrawables

        val context = editText.context.applicationContext
        val drawable = ContextCompat.getDrawable(context, drawableId)

        drawable?.setBounds(
                0,
                0,
                drawableWidth.toInt(),
                drawableHeight.toInt())

        editText.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    }

}

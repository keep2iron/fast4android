package io.github.keep2iron.android.comp.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/22 14:24
 */
class TypeFaceTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
//        val app = getContext().applicationContext as LoggerInitTask
//        val fromAsset = app.getTag(TYPE_ID) as Typeface?
//        if (fromAsset != null) {
//            typeface = fromAsset
//        }
    }

    companion object {
        const val TYPE_ID = 101
    }
}
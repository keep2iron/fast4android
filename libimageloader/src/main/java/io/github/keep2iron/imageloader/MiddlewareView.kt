package io.github.keep2iron.imageloader

import android.content.Context
import android.util.AttributeSet
import com.facebook.drawee.view.SimpleDraweeView

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/06/25 20:23
 */
open class MiddlewareView : SimpleDraweeView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

}
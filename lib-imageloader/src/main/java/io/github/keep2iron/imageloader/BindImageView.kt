package io.github.keep2iron.imageloader

import android.databinding.BindingAdapter

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/21 13:25
 */
object BindImageView {

    @JvmStatic
    @BindingAdapter("url", requireAll = true)
    fun bindImageViewByUrl(view: MiddlewareView, url: String) {
        val imageLoader = ImageLoaderManager.getImageLoader()
        imageLoader.showImageView(view, url = url)
    }
}

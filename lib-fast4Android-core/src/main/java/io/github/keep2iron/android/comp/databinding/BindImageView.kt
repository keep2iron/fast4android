package io.github.keep2iron.android.comp.databinding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.widget.ImageView
import io.github.keep2iron.android.comp.glide.ImageLoader

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/21 13:25
 */
object BindImageView {

    @BindingAdapter(value = *arrayOf("url", "place_holder", "width", "height"), requireAll = false)
    fun bindImageViewByUrl(imageView: AppCompatImageView, url: String, width: Float, height: Float, placeHolder: Drawable) {
        ImageLoader.create(imageView).apply {
            this.url = url
            this.placeHolderDrawable = placeHolder
        }.into(imageView)
    }

    @BindingAdapter(value = *arrayOf("url", "width", "height"), requireAll = false)
    fun bindImageViewByUrl(imageView: AppCompatImageView, url: String, width: Float, height: Float) {
        ImageLoader.create(imageView).apply {
            this.url = url
        }.into(imageView)
    }

    @BindingAdapter(value = *arrayOf("url", "width", "height", "round"), requireAll = false)
    fun bindImageViewByUrl(imageView: AppCompatImageView, url: String, width: Float, height: Float, round: Float) {
        ImageLoader.create(imageView).apply {
            this.url = url
            this.round = round.toInt()
        }.into(imageView)
    }

    @BindingAdapter(value = *arrayOf("oval_url", "place_holder"), requireAll = false)
    fun bindImageViewByOval(imageView: ImageView, url: String, placeHolder: Int) {
        ImageLoader.create(imageView).apply {
            this.url = url
            this.placeHolderResId = placeHolder
        }.into(imageView)
    }

}

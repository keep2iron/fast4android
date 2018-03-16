package io.github.keep2iron.android.comp.glide

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView

import com.bumptech.glide.request.target.ImageViewTarget

/**
 *
 * 设置 默认图片，异常图片，指定路径的图片为圆角图片
 */

class CircleBitmapTarget(view: ImageView) : ImageViewTarget<Bitmap>(view) {

    /**
     * 从指定路径加载的Bitmap
     * @param resource
     */
    override fun setResource(resource: Bitmap?) {
        bindCircleBitmapToImageView(resource)
    }

    /**
     *
     * onLoadFailed()和onLoadStarted调用该方法，用于设置默认的图片和异常图片
     * 设置默认图片
     * @param drawable
     */
    override fun setDrawable(drawable: Drawable) {
        if (drawable is BitmapDrawable) {
            val bitmap1 = drawable.bitmap
            bindCircleBitmapToImageView(bitmap1)
        } else {
            view.setImageDrawable(drawable)
        }
    }

    /**
     * 通过RoundedBitmapDrawable绘制圆形Bitmap,且加载ImageView.
     * @param bitmap
     */
    private fun bindCircleBitmapToImageView(bitmap: Bitmap?) {
        val bitmapDrawable = RoundedBitmapDrawableFactory.create(view.context.resources, bitmap)
        bitmapDrawable.isCircular = true
        view.setImageDrawable(bitmapDrawable)
    }
}
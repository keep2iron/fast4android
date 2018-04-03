package io.github.keep2iron.android.comp.glide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/02 10:05
 */
class ImageLoader(private var request: RequestManager) {
    var url: String? = null
    var round: Int = 0

    var isCircle: Boolean = false

    @DrawableRes
    var placeHolderResId: Int = 0
    @DrawableRes
    var errorImageResId: Int = 0
    @DrawableRes
    var fallbackResId: Int = 0

    var placeHolderDrawable: Drawable? = null

    var scaleType: ScaleType = ScaleType.CENTER_CROP


    companion object {
        fun create(activity: Activity): ImageLoader {
            return ImageLoader(GlideApp.with(activity))
        }

        fun create(fragment: Fragment): ImageLoader {
            return ImageLoader(GlideApp.with(fragment))
        }

        fun create(context: Context): ImageLoader {
            return ImageLoader(GlideApp.with(context))
        }

        fun create(view: View): ImageLoader {
            return ImageLoader(GlideApp.with(view))
        }
    }

    fun into(imageView: ImageView) {
        val options: RequestOptions
        if (isCircle) {
            options = RequestOptions.circleCropTransform()
        } else {
            options = RequestOptions()

            if (round > 0) {
                options.transform(RoundedCorners(round))
            }
        }

        if (placeHolderResId > 0) {
            options.placeholder(placeHolderResId)
        }
        if (errorImageResId > 0) {
            options.error(errorImageResId)
        }
        if (fallbackResId > 0) {
            options.fallback(fallbackResId)
        }

        when (scaleType) {
            ScaleType.CENTER_CROP -> {
                options.centerCrop()
            }
            ScaleType.CENTER_INSIDE -> {
                options.centerInside()
            }
            ScaleType.FIT_CENTER -> {
                options.fitCenter()
            }
        }

        request.load(url).transition(withCrossFade()).apply(options).into(imageView)
    }

    enum class ScaleType {
        CENTER_CROP,
        CENTER_INSIDE,
        FIT_CENTER
    }
}
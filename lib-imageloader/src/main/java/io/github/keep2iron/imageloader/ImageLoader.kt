package com.bana.libcore.image

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import io.github.keep2iron.imageloader.CloseableImageWrapper
import io.github.keep2iron.imageloader.ImageLoaderOptions
import io.github.keep2iron.imageloader.MiddlewareView

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/06/25 19:54
 */
interface ImageLoader {
    /**
     * 该方法必须在主线程并且最好在application中初始化
     */
    fun init(context: Application)

    /**
     * 显示imageView
     */
    fun showImageView(imageView: MiddlewareView,
                      url: String,
                      options: ImageLoaderOptions = ImageLoaderOptions.getDefaultOption())

    fun showImageView(imageView: MiddlewareView,
                      uri: Uri,
                      options: ImageLoaderOptions = ImageLoaderOptions.getDefaultOption())

//    fun showBlurImageView(imageView: MiddlewareView,
//                          url: String,
//                          options: ImageLoaderOptions = ImageLoaderOptions.getDefaultOption(),
//                          iterations: Int,
//                          blurRadius: Int)


    /**
     * 获取网络url指定的Bitmap
     */
    fun getImage(context: Context,
                 url: String,
                 options: ImageLoaderOptions = ImageLoaderOptions.getDefaultOption(),
                 onGetBitmap: (CloseableImageWrapper?) -> Unit)

    fun getBitmap(context: Context, url: String, options: ImageLoaderOptions = ImageLoaderOptions.getDefaultOption(), onGetBitmap: (Bitmap?) -> Unit)


    /**
     * 清理内存
     */
    fun cleanMemory(context: Context)

    /**
     * 暂停所有下载
     */
    fun pause(context: Context)

    /**
     * 回复所有下载
     */
    fun resume(context: Context)

    /**
     * 清除所有缓存
     */
    fun clearAllCache()

    /**
     * 获取配置
     */
    fun getConfig(): Any
}
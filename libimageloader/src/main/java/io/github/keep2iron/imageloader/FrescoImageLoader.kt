package io.github.keep2iron.imageloader

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.bana.libcore.image.ImageLoader
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.logging.FLog
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.drawee.generic.RoundingParams
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private class ExecutorManager private constructor() {
    companion object {
        val cacheExecutor: ExecutorService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Executors.newCachedThreadPool()
        }
    }
}

class MatrixScaleType(private val matrix: Matrix) : ScalingUtils.ScaleType {

    override fun getTransform(outTransform: Matrix,
                              parentBounds: Rect,
                              childWidth: Int,
                              childHeight: Int,
                              focusX: Float,
                              focusY: Float): Matrix {
        val sX = parentBounds.width().toFloat() / childWidth
        val sY = parentBounds.height().toFloat() / childHeight
        val scale = Math.max(sX, sY)
        outTransform.postConcat(matrix)
        outTransform.postScale(scale, scale)
        return outTransform
    }
}

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/06/25 20:12
 */
class FrescoImageLoader : ImageLoader {
    lateinit var config: ImagePipelineConfig
    private val handler: Handler = Handler()

    override fun getConfig(): Any {
        return config
    }

    override fun init(context: Application) {
        val createMemoryCacheParams = {
            val maxHeapSize = Runtime.getRuntime().maxMemory().toInt()
            val maxMemoryCacheSize = maxHeapSize / 3 * 2//取手机内存最大值的三分之二作为可用的最大内存数
            MemoryCacheParams( //
                    // 可用最大内存数，以字节为单位
                    maxMemoryCacheSize,
                    // 内存中允许的最多图片数量
                    200,
                    // 内存中准备清理但是尚未删除的总图片所可用的最大内存数，以字节为单位
                    Integer.MAX_VALUE,
                    // 内存中准备清除的图片最大数量
                    Integer.MAX_VALUE,
                    // 内存中单图片的最大大小
                    Integer.MAX_VALUE)
        }
        val imagePipelineConfigBuilder = ImagePipelineConfig.newBuilder(context)
        imagePipelineConfigBuilder.setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.cacheDir)//设置磁盘缓存的路径
                .setBaseDirectoryName("cache_images")//设置磁盘缓存文件夹的名称
                .setMaxCacheSize((200 * ByteConstants.MB).toLong())//设置磁盘缓存的大小
                .build())
        imagePipelineConfigBuilder.isDownsampleEnabled = true
        //设置已解码的内存缓存（Bitmap缓存）
        imagePipelineConfigBuilder.setBitmapMemoryCacheParamsSupplier {
            return@setBitmapMemoryCacheParamsSupplier createMemoryCacheParams()
        }
        //设置未解码的内存缓存
        imagePipelineConfigBuilder.setEncodedMemoryCacheParamsSupplier {
            return@setEncodedMemoryCacheParamsSupplier createMemoryCacheParams()
        }
        //设置内存紧张时的应对措施
        val memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance()
        memoryTrimmableRegistry.registerMemoryTrimmable { trimType ->
            val suggestedTrimRatio = trimType.suggestedTrimRatio
            if (MemoryTrimType.OnCloseToDalvikHeapLimit.suggestedTrimRatio == suggestedTrimRatio
                    || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.suggestedTrimRatio == suggestedTrimRatio
                    || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.suggestedTrimRatio == suggestedTrimRatio) {
                //清空内存缓存
                ImagePipelineFactory.getInstance().imagePipeline.clearMemoryCaches()
            }
        }
        imagePipelineConfigBuilder.setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
        imagePipelineConfigBuilder.setMemoryTrimmableRegistry(memoryTrimmableRegistry)
        imagePipelineConfigBuilder.isDownsampleEnabled = true
        imagePipelineConfigBuilder.setBitmapsConfig(Bitmap.Config.RGB_565)

        this.config = imagePipelineConfigBuilder.build()
        Fresco.initialize(context.applicationContext)
        if (BuildConfig.DEBUG) {
            FLog.setMinimumLoggingLevel(FLog.VERBOSE)
        }
    }

    override fun showImageView(imageView: MiddlewareView, uri: Uri, options: ImageLoaderOptions) {
        val draweeView = imageView as SimpleDraweeView
        val requestBuilder = buildImageRequest(uri, options)
        if (options.iterations > 0 && options.blurRadius > 0) {
            requestBuilder.postprocessor = IterativeBoxBlurPostProcessor(options.iterations, options.blurRadius)
        }
        val controllerBuilder = buildController(requestBuilder, draweeView, options)

        options.smallImageUri?.let { smallUrl ->
            controllerBuilder.lowResImageRequest = buildImageRequest(Uri.parse(smallUrl), options).build()
        }
        val controller = controllerBuilder.build()
        setImageLoaderOptions(options, draweeView)
        draweeView.controller = controller
    }

    private fun buildController(request: ImageRequestBuilder,
                                draweeView: SimpleDraweeView,
                                options: ImageLoaderOptions): PipelineDraweeControllerBuilder {
        val controllerBuilder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request.build())
                .setOldController(draweeView.controller)

        if (options.isSetByImageSize && options.imageWidth <= 0) {
            throw IllegalArgumentException("if you set options isSetByImageSize,you must set imageWidth,because compute height dependency by imageWidth.")
        }

        val controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                if (options.isSetByImageSize) {
                    if (imageInfo == null) {
                        return
                    }
                    val layoutParams = draweeView.layoutParams
                    val height = imageInfo.height
                    val width = imageInfo.width
                    layoutParams.width = options.imageWidth
                    if (options.imageHeight <= 0) {
                        layoutParams.height = (options.imageWidth.toFloat() / width * height).toInt()
                    } else {
                        layoutParams.height = options.imageHeight
                    }
                    draweeView.layoutParams = layoutParams
                }
                options.onFinalImageSetListener?.invoke()
            }

            override fun onFailure(id: String?, throwable: Throwable?) {
                super.onFailure(id, throwable)

                options.onImageFailure?.invoke()
            }
        }
        controllerBuilder.controllerListener = controllerListener
        return controllerBuilder
    }

    override fun showImageView(imageView: MiddlewareView, url: String, options: ImageLoaderOptions) {
        showImageView(imageView, Uri.parse(url), options)
    }

    private fun buildImageRequest(uri: Uri, options: ImageLoaderOptions): ImageRequestBuilder {
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                //本功能仅支持本地URI，并且是JPEG图片格式 如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
                .setLocalThumbnailPreviewsEnabled(true)
                //渐进式加载
                .setProgressiveRenderingEnabled(options.isProgressiveLoadImage)
        if (options.imageWidth > 0 && options.imageHeight > 0) {
            request.resizeOptions = ResizeOptions(options.imageWidth, options.imageHeight)
        }
        return request
    }

    override fun getBitmap(context: Context, url: String, options: ImageLoaderOptions, onGetBitmap: (Bitmap?) -> Unit) {
        val request = buildImageRequest(Uri.parse(url), options)
        val imagePipeline = Fresco.getImagePipeline()
        val dataSource = imagePipeline.fetchDecodedImage(request.build(), context.applicationContext)
        dataSource.subscribe(object : BaseBitmapDataSubscriber() {
            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                Looper.getMainLooper().thread
                handler.post {
                    onGetBitmap(null)
                }
            }

            override fun onNewResultImpl(bitmap: Bitmap?) {
                val copyBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)
                handler.post {
                    onGetBitmap(copyBitmap)
                }
            }
        }, ExecutorManager.cacheExecutor)
    }

    override fun getImage(context: Context,
                          url: String,
                          options: ImageLoaderOptions,
                          onGetBitmap: (CloseableImageWrapper?) -> Unit) {
        val request = buildImageRequest(Uri.parse(url), options)
        val imagePipeline = Fresco.getImagePipeline()
        val dataSource = imagePipeline.fetchDecodedImage(request.build(), context.applicationContext)
        dataSource.subscribe(object : BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                onGetBitmap(null)
            }

            override fun onNewResultImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                onGetBitmap(CloseableImageWrapper(dataSource.result))
            }
        }, ExecutorManager.cacheExecutor)
    }

    private fun setImageLoaderOptions(options: ImageLoaderOptions, draweeView: SimpleDraweeView) {
        val builder = GenericDraweeHierarchyBuilder(draweeView.context.resources)
        val hierarchy = builder
                .setFadeDuration(300)
                .build()
        draweeView.hierarchy = hierarchy

        if (options.isCircleImage) {
            loadCircleImage(draweeView)
        }
        if (options.radius > 0) {
            loadRadiusImage(draweeView, options.radius)
        }
        if (options.borderOverlayColor != -1) {
            setBorder(draweeView, options.borderOverlayColor, options.borderSize)
        }
        if (options.scaleType != ImageLoaderOptions.ScaleType.NONE) {
            setMode(draweeView, options)
        }
    }

    private fun setMode(draweeView: SimpleDraweeView, options: ImageLoaderOptions) {
        val optionScaleType = when (options.scaleType) {
            ImageLoaderOptions.ScaleType.CENTER -> ScalingUtils.ScaleType.CENTER
            ImageLoaderOptions.ScaleType.CENTER_CROP -> ScalingUtils.ScaleType.CENTER_CROP
            ImageLoaderOptions.ScaleType.FOCUS_CROP -> ScalingUtils.ScaleType.FOCUS_CROP
            ImageLoaderOptions.ScaleType.CENTER_INSIDE -> ScalingUtils.ScaleType.CENTER_INSIDE
            ImageLoaderOptions.ScaleType.FIT_CENTER -> ScalingUtils.ScaleType.FIT_CENTER
            ImageLoaderOptions.ScaleType.FIT_START -> ScalingUtils.ScaleType.FIT_START
            ImageLoaderOptions.ScaleType.FIT_END -> ScalingUtils.ScaleType.FIT_END
            ImageLoaderOptions.ScaleType.FIT_XY -> ScalingUtils.ScaleType.FIT_XY
            //默认fitCenter
            ImageLoaderOptions.ScaleType.NONE -> ScalingUtils.ScaleType.FIT_CENTER
            ImageLoaderOptions.ScaleType.MATRIX -> MatrixScaleType(options.matrix!!)
        }
        draweeView.hierarchy.actualImageScaleType = optionScaleType
    }


    private fun loadCircleImage(draweeView: SimpleDraweeView) {
        val asCircle = RoundingParams.asCircle()
        draweeView.hierarchy.roundingParams = asCircle
    }

    private fun loadRadiusImage(draweeView: SimpleDraweeView, radius: Float) {
        val cornersRadius = RoundingParams.fromCornersRadius(radius)
        draweeView.hierarchy.roundingParams = cornersRadius
    }

    private fun setBorder(draweeView: SimpleDraweeView, color: Int, borderSize: Float) {
        val roundingParams = draweeView.hierarchy.roundingParams
                ?: throw IllegalArgumentException("you must set radius or set a circle image!!")
        if (borderSize <= 0) {
            throw IllegalArgumentException("do you forget set a borderSize?")
        }
        roundingParams.setBorder(color, borderSize)
    }

    override fun cleanMemory(context: Context) {
        //清空内存缓存
        ImagePipelineFactory.getInstance().imagePipeline.clearMemoryCaches()
    }

    override fun clearAllCache() {
        val imagePipeline = Fresco.getImagePipeline()
        imagePipeline.clearCaches()
    }

    override fun pause(context: Context) {
    }

    override fun resume(context: Context) {
    }
}
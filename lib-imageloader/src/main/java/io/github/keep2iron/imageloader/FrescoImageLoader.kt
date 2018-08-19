package io.github.keep2iron.imageloader

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.drawee.generic.RoundingParams
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import java.util.concurrent.Executors

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/06/25 20:12
 */
class FrescoImageLoader : ImageLoader {

    override fun init(context: Application) {
        val createMemoryCacheParams = {
            val maxHeapSize = Runtime.getRuntime().maxMemory().toInt()
            val maxMemoryCacheSize = maxHeapSize / 5//取手机内存最大值的5分之一作为可用的最大内存数
            MemoryCacheParams( //
                    // 可用最大内存数，以字节为单位
                    maxMemoryCacheSize,
                    // 内存中允许的最多图片数量
                    30,
                    // 内存中准备清理但是尚未删除的总图片所可用的最大内存数，以字节为单位
                    Integer.MAX_VALUE,
                    // 内存中准备清除的图片最大数量
                    Integer.MAX_VALUE,
                    // 内存中单图片的最大大小
                    Integer.MAX_VALUE)
        }
        val imagePipelineConfigBuilder = ImagePipelineConfig.newBuilder(context)
        imagePipelineConfigBuilder.setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.externalCacheDir)//设置磁盘缓存的路径
                .setBaseDirectoryName("cache_images")//设置磁盘缓存文件夹的名称
                .setMaxCacheSize((60 * ByteConstants.MB).toLong())//设置磁盘缓存的大小
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

        Fresco.initialize(context.applicationContext, imagePipelineConfigBuilder.build())
    }


    override fun showImageView(imageView: MiddlewareView, url: String, options: ImageLoaderOptions) {
        val draweeView = imageView as SimpleDraweeView
        val request = buildImageRequest(url)
        val controllerBuilder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.controller)
        options.smallImageUri?.let { smallUrl ->
            controllerBuilder.lowResImageRequest = buildImageRequest(smallUrl)
        }
        val controller = controllerBuilder.build()
        setImageLoaderOptions(options, draweeView)
        draweeView.controller = controller
    }

    override fun showBlurImageView(imageView: MiddlewareView, url: String, options: ImageLoaderOptions, iterations: Int, blurRadius: Int) {
        val draweeView = imageView as SimpleDraweeView
        val uri = Uri.parse(url)
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .setPostprocessor(IterativeBoxBlurPostProcessor(iterations, blurRadius))
                .build()
        val controller =
                Fresco.newDraweeControllerBuilder()
                        .setOldController(draweeView.controller)
                        .setImageRequest(request)
                        .build()

        setImageLoaderOptions(options, draweeView)
        draweeView.controller = controller
    }

    private fun buildImageRequest(url: String): ImageRequest {
        val uri: Uri = Uri.parse(url)
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build()
    }

    override fun getImage(context: Context,
                          url: String,
                          options: ImageLoaderOptions,
                          onGetBitmap: (CloseableImageWrapper?) -> Unit) {
        val request = buildImageRequest(url)
        val imagePipeline = Fresco.getImagePipeline()
        val dataSource = imagePipeline.fetchDecodedImage(request, context.applicationContext)
        dataSource.subscribe(object : BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                onGetBitmap(null)
            }

            override fun onNewResultImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                onGetBitmap(CloseableImageWrapper(dataSource.result))
            }
        }, Executors.newCachedThreadPool())
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
            setMode(draweeView, options.scaleType)
        }
    }


    private fun setMode(draweeView: SimpleDraweeView, scaleType: ImageLoaderOptions.ScaleType) {
        val optionScaleType = when (scaleType) {
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
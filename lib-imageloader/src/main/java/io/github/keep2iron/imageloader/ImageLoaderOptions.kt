package io.github.keep2iron.imageloader

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/06/25 19:59
 */
class ImageLoaderOptions {

    companion object {
        fun getDefaultOption(): ImageLoaderOptions {
            return ImageLoaderOptions()
        }
    }

    var imageWidth: Int = -1
    var imageHeight: Int = -1
    /**
     * 圆角
     */
    var radius: Float = -1f
    /**
     * 是否以渐进式方式加载图片 ，仅Fresco支持
     */
    var isProgressiveLoadImage = false
    /**
     * 是否为圆形图片
     */
    var isCircleImage = false
    /**
     * 图片边界颜色
     */
    var borderOverlayColor = -1
    /**
     * 边界颜色
     */
    var borderSize: Float = -1f
    /**
     * 显示mode
     */
    var scaleType: ScaleType = ScaleType.NONE

    var smallImageUri: String? = null

//    /**
//     * 高斯模糊的迭代次数,次数越高越魔性
//     */
//    var iterations = 0
//    /**
//     * 模糊半径 越高越模糊
//     */
//    var blurRadius = 0

    /**
     * if use fresco see http://frescolib.org/docs/scaletypes.html
     */
    enum class ScaleType {
        CENTER,
        CENTER_CROP,
        FOCUS_CROP,
        CENTER_INSIDE,
        FIT_CENTER,
        FIT_START,
        FIT_END,
        FIT_XY,
        NONE,
    }
}
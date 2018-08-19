package io.github.keep2iron.imageloader

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/06/25 19:42
 */
class ImageLoaderManager private constructor() {

    companion object {
        @JvmStatic
        private val instance: ImageLoader by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FrescoImageLoader()
        }

        @JvmStatic
        fun getImageLoader(): ImageLoader {
            return instance
        }
    }
}
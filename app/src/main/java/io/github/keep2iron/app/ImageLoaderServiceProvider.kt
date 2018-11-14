package io.github.keep2iron.app

import android.app.Application
import io.github.keep2iron.android.ComponentServiceProvider
import io.github.keep2iron.pineapple.ImageLoaderManager

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/29
 */
class ImageLoaderServiceProvider : ComponentServiceProvider<ImageLoaderManager> {

    override fun provideComponentService(application: Application): ImageLoaderManager {
        ImageLoaderManager.init(application)
        return ImageLoaderManager.INSTANCE
    }

    override val componentName: String = "ImageLoaderManager"
}
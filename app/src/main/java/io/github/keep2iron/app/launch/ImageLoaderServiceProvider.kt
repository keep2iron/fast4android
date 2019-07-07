package io.github.keep2iron.app.launch

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
    override fun providerComponentServiceClass(): Class<ImageLoaderManager> = ImageLoaderManager::class.java

    override fun provideComponentService(application: Application): ImageLoaderManager {
        ImageLoaderManager.init(application)
        return ImageLoaderManager.getInstance()
    }
}
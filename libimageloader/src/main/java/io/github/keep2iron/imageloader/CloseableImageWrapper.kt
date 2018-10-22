package io.github.keep2iron.imageloader

import android.graphics.Bitmap
import com.facebook.common.references.CloseableReference
import com.facebook.imagepipeline.image.CloseableBitmap
import com.facebook.imagepipeline.image.CloseableImage

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/07/03 11:51
 */
class CloseableImageWrapper(private var ref: CloseableReference<CloseableImage>?) {

    fun getBitmap(): Bitmap? {
        val closeableImage = ref!!.get()
        return if (closeableImage is CloseableBitmap) {
            // do something with the bitmap
            closeableImage.underlyingBitmap
        } else {
            null
        }
    }

    fun close() {
        CloseableReference.closeSafely(ref)
        ref = null
    }
}
package io.github.keep2iron.android.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/12 14:52
 */
class LifecycleViewModelFactory(private val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {

    private var appContext: Context? = null

    constructor(context: Context, lifecycleOwner: LifecycleOwner) : this(lifecycleOwner) {
        appContext = context.applicationContext
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.getDeclaredConstructor(LifecycleOwner::class.java).newInstance(lifecycleOwner)
        } catch (ex: NoSuchMethodException) {
            if(appContext != null) {
                modelClass.getDeclaredConstructor(LifecycleOwner::class.java, Context::class.java).newInstance(lifecycleOwner, appContext)
            }else{
                throw IllegalArgumentException("Context is null !! please call LifecycleViewModelFactory(context: Context, lifecycleOwner: LifecycleOwner) to set a Context ")
            }
        }
    }
}

/*
 * Create bt Keep2iron on 17-6-22 下午4:01
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import io.github.keep2iron.android.R
import io.github.keep2iron.android.core.annotation.StatusColor
import io.github.keep2iron.android.core.rx.LifecycleEvent
import io.github.keep2iron.android.core.rx.RxLifecycle
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron
 * @date 2017/4/12
 * 这里的BaseActivity没有包含
 */
/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/05/19 10:36
 */
abstract class AbstractActivity<DB : ViewDataBinding> : AppCompatActivity() {
    private var subject = BehaviorSubject.create<LifecycleEvent>()

    private lateinit var immersionBar: ImmersionBar
    protected lateinit var dataBinding: DB

    protected open fun beforeInit(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit()
        val createDatabinding: DB? = DataBindingUtil.setContentView(this, getResId())
        if (createDatabinding != null) {
            dataBinding = createDatabinding
        } else {
            setContentView(getResId())
        }
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        setStatusColorFromAnnotation()

        initVariables(savedInstanceState)
        subject.onNext(LifecycleEvent.CREATE)
    }


    internal fun setStatusColorFromAnnotation() {
        immersionBar = ImmersionBar
                .with(this)

        val annotations = this::class.java.annotations
        for (annotation in annotations) {
            if (annotation is StatusColor) {
                val annStatusColor: StatusColor = annotation
                immersionBar.fitsSystemWindows(annotation.isFitSystem)

                val value = annStatusColor.value
                val darkMode = annStatusColor.isDarkMode

                if (value != -1) {
                    immersionBar.statusBarColor(value)
                            //修改flyme OS状态栏字体颜色
                            .flymeOSStatusBarFontColor(value)
                }
                if (annStatusColor.isTrans) {
                    immersionBar.transparentStatusBar()
                }
                if (annStatusColor.navigationBarColor != -1) {
                    immersionBar.navigationBarColor(annStatusColor.navigationBarColor)
                } else {
                    immersionBar.navigationBarColor("#000000")
                }
                immersionBar.statusBarDarkFont(darkMode)
                immersionBar.addTag("default")
            }
        }
        immersionBar.init()   //所有子类都将继承这些相同的属性
    }

    fun setStatusColor(@ColorRes color: Int) {
        immersionBar.getTag("defalut")
                .statusBarColor(color)
                .init()
    }

    fun setStateTextColor(isDark: Boolean) {
        immersionBar.getTag("default")
                .statusBarDarkFont(isDark)
                .init()
    }

    fun getImmersionBar(): ImmersionBar {
        return immersionBar.getTag("default")
    }

    override fun onStart() {
        super.onStart()
        subject.onNext(LifecycleEvent.START)
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_START)

    }

    override fun onResume() {
        super.onResume()
        subject.onNext(LifecycleEvent.RESUME)
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onPause() {
        super.onPause()
        subject.onNext(LifecycleEvent.PAUSE)
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()
        subject.onNext(LifecycleEvent.STOP)
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        subject.onNext(LifecycleEvent.DESTROY)
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        immersionBar.destroy()
        super.onDestroy()
    }

    /**
     * 绑定让订阅进行绑定生命周期
     */
    fun <T> bindObservableLifeCycle(): ObservableTransformer<T, T> {
        return RxLifecycle.bindUntilEvent(subject, LifecycleEvent.DESTROY)
    }


    /**
     * 获取资源id
     *
     * @return 资源id
     */
    @LayoutRes
    protected abstract fun getResId(): Int

    /**
     * 在这个方法中可以进行重新 编写控件的逻辑
     */
    abstract fun initVariables(savedInstanceState: Bundle?)
}
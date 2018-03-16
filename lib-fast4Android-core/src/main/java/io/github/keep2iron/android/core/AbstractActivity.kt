/*
 * Create bt Keep2iron on 17-6-22 下午4:01
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.barlibrary.ImmersionBar

import io.github.keep2iron.android.core.annotation.StatusColor
import io.github.keep2iron.android.core.rx.LifecycleEvent
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron
 * @date 2017/4/12
 * 这里的BaseActivity没有包含
 */
abstract class AbstractActivity<DB : ViewDataBinding> : AppCompatActivity(), LifecycleOwner {
    private var subject = BehaviorSubject.create<LifecycleEvent>()
    private var immersionBar: ImmersionBar? = null
    protected lateinit var dataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        dataBinding = DataBindingUtil.setContentView(this, getResId())
        if (null == dataBinding) {
            throw IllegalArgumentException("do your declare <layout></layout> in your xml file")
        }
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        setStatusColor()

        initVariables(savedInstanceState)
        subject.onNext(LifecycleEvent.CREATE)
    }


    fun setStatusColor() {
        for (annotation in this::class.annotations) {
            if (annotation is StatusColor) {
                val annStatusColor: StatusColor = annotation
                immersionBar = ImmersionBar.with(this)
                        .fitsSystemWindows(true)

                val value = annStatusColor.value
                val darkMode = annStatusColor.isDarkMode

                if (value != -1) {
                    immersionBar!!.statusBarColor(value)
                            //修改flyme OS状态栏字体颜色
                            .flymeOSStatusBarFontColor(value)
                }
                if (annStatusColor.isTrans) {
                    immersionBar!!.transparentStatusBar()
                            .fitsSystemWindows(false)
                }
                immersionBar!!.statusBarDarkFont(darkMode)
                immersionBar!!.addTag("default")
                immersionBar!!.init()   //所有子类都将继承这些相同的属性
            }
        }
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
        if (immersionBar != null) {
            immersionBar!!.destroy()
        }
        super.onDestroy()
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
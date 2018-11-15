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
import io.github.keep2iron.android.annotation.StatusColor
import io.github.keep2iron.android.rx.LifecycleEvent
import io.github.keep2iron.android.rx.RxLifecycle
import io.github.keep2iron.android.utilities.RxTransUtil
import io.reactivex.FlowableTransformer
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

    protected open fun beforeInit() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit()
        val createDataBinding: DB? = DataBindingUtil.setContentView(this, resId)
        if (createDataBinding != null) {
            dataBinding = createDataBinding
        } else {
            setContentView(resId)
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
    @get:LayoutRes
    protected abstract val resId: Int

    /**
     * 在这个方法中可以进行重新 编写控件的逻辑
     */
    abstract fun initVariables(savedInstanceState: Bundle?)

    fun <T> rxObservableScheduler(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.compose(RxTransUtil.rxObservableScheduler())
                    .compose(RxLifecycle.bindUntilEvent(this.subject, LifecycleEvent.DESTROY))
        }
    }

    fun <T> bindFlowableLifeCycle(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.compose(RxTransUtil.rxFlowableScheduler())
                    .compose(RxLifecycle.bindUntilEvent(this.subject, LifecycleEvent.DESTROY))
        }
    }
}
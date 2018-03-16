package io.github.keep2iron.android.comp

import java.lang.reflect.InvocationTargetException
import java.util.HashMap
import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.util.ToastUtil

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/10/13 15:40
 *
 *
 * 获取其他组件的管理器对象
 * 1.注册组件
 * 2.通过组件的完整类名，获取组件
 */
class ComponentManager private constructor() {

    /**
     * 暴露组件的服务的相关接口
     */
    private val mServiceMap = HashMap<String, Class<*>>()

    fun registerService(serviceName: String, exportService: Class<*>) {
        mServiceMap[serviceName] = exportService
    }

    /**
     * 获取组件服务
     *
     * @param serviceKey 组件暴露的key
     * @return 组件实例
     */
    fun getService(serviceKey: String): Any? {
        val aClass = mServiceMap[serviceKey]
        if (aClass == null) {
            ToastUtil.S("do your call registerService() for " + serviceKey)
            return null
        }

        try {
            return aClass.getConstructor().newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 该方法的调用时机需要放在Application的onCreate中执行
     *
     * @param application    被依附的application
     * @param componentNames 编译的组件名称
     */
    fun init(application: AbstractApplication, componentNames: Array<String>) {

        for (compName in componentNames) {
            try {
                val clazz = Class.forName(compName) as Class<out IComponentLifeCycle>
                val lifeCycle = clazz.getConstructor().newInstance()
                lifeCycle.onAttachApp(application)
                lifeCycle.onRegisterService(this)
                AbstractApplication.instance.mIComponentLifeCycles.add(lifeCycle)
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(compName + " can't found")
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            }

        }
    }

    companion object {
        @Volatile
        private var mInstance: ComponentManager? = null

        val instance: ComponentManager?
            get() {
                if (mInstance == null) {
                    synchronized(ComponentManager::class.java) {
                        if (mInstance == null) {
                            mInstance = ComponentManager()
                        }
                    }
                }

                return mInstance
            }
    }
}
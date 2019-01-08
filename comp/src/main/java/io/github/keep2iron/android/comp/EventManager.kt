package io.github.keep2iron.android.comp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.util.ArrayMap
import io.github.keep2iron.android.Fast4Android
import java.io.Serializable

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2019/1/7
 */
class EventManager {

    private val lifeCycleBroadcastMap = ArrayMap<String, BroadcastReceiver>()

    companion object {
        const val DEFAULT_KEY = "Default_Key"

        private val INSTANCE: EventManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            EventManager()
        }

        fun getInstance(): EventManager {
            return EventManager.INSTANCE
        }
    }

    fun register(lifecycleOwner: LifecycleOwner, action: String, handler: (Context, Intent) -> Unit) {
        val broadcastReceiver = InnerBroadcastReceiver(handler)
        val intentFilter = IntentFilter(action)
        LocalBroadcastManager.getInstance(Fast4Android.CONTEXT).registerReceiver(broadcastReceiver, intentFilter)

        lifeCycleBroadcastMap[lifecycleOwner.hashCode().toString() + "#$action"] = broadcastReceiver

        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                unregisterAll(lifecycleOwner)
            }
        })
    }

    fun unregisterAll(lifecycleOwner: LifecycleOwner) {
        lifeCycleBroadcastMap.keys.forEach {
            if (it.startsWith(lifecycleOwner.hashCode().toString())) {
                val broadcastReceiver = lifeCycleBroadcastMap[it]
                if (broadcastReceiver != null) {
                    LocalBroadcastManager.getInstance(Fast4Android.CONTEXT).unregisterReceiver(broadcastReceiver)
                    lifeCycleBroadcastMap[it] = null
                    lifeCycleBroadcastMap.remove(it)
                }
            }
        }
    }

    fun sendEvent(action: String, data: Serializable) {
        val intent = Intent(action)
        intent.putExtra(DEFAULT_KEY, data)
        LocalBroadcastManager.getInstance(Fast4Android.CONTEXT).sendBroadcast(intent)
    }

    fun sendEvent(action: String, data: Parcelable) {
        val intent = Intent(action)
        intent.putExtra(DEFAULT_KEY, data)
        LocalBroadcastManager.getInstance(Fast4Android.CONTEXT).sendBroadcast(intent)
    }

    fun sendEvent(action: String, bundle: Bundle) {
        val intent = Intent(action)
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(Fast4Android.CONTEXT).sendBroadcast(intent)
    }

    private class InnerBroadcastReceiver(val handler: (Context, Intent) -> Unit) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handler.invoke(context, intent)
        }
    }
}
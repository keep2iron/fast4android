package io.github.keep2iron.fast4android.comp

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

/**
 *
 */
class EventManager private constructor(private val lifecycleOwner: LifecycleOwner) {

  private val publishSubject: PublishSubject<Intent> = PublishSubject.create()

  fun register(vararg actions: String): PublishSubject<Intent> {
    val receiver = getModifyEvent()
    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
      @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
      fun onDestroy() {
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(CONTEXT).unregisterReceiver(receiver)
      }
    })
    val filter = IntentFilter()
    val i = actions.iterator()
    while (i.hasNext()) {
      filter.addAction(i.next())
    }
    androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(CONTEXT).registerReceiver(receiver, filter)
    return publishSubject
  }

  private fun getModifyEvent(): BroadcastReceiver {
    return object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        publishSubject.onNext(intent)
      }
    }
  }

  companion object {

    private lateinit var CONTEXT: Application

    @JvmStatic
    fun init(application: Application) {
      this.CONTEXT = application
    }

    @JvmStatic
    fun observerEvent(lifecycleOwner: LifecycleOwner): EventManager {
      return EventManager(lifecycleOwner)
    }

    @JvmStatic
    fun sendEvent(action: String, vararg params: Pair<String, Any>) {
      val intent = Intent(action)
      val manager = androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(CONTEXT)
      for (arg in params) {
        val value = arg.second
        when (value) {
          is String -> {
            intent.putExtra(arg.first, value)
          }
          is Int -> {
            intent.putExtra(arg.first, value)
          }
          is Double -> {
            intent.putExtra(arg.first, value)
          }
          is Float -> {
            intent.putExtra(arg.first, value)
          }
          is Byte -> {
            intent.putExtra(arg.first, value)
          }
          is Boolean -> {
            intent.putExtra(arg.first, value)
          }
          is Bundle -> {
            intent.putExtra(arg.first, value)
          }
          is Long -> {
            intent.putExtra(arg.first, value)
          }
          is Char -> {
            intent.putExtra(arg.first, value)
          }
          is Short -> {
            intent.putExtra(arg.first, value)
          }
          is Parcelable -> {
            intent.putExtra(arg.first, value)
          }
          is IntArray -> {
            intent.putExtra(arg.first, value)
          }
          is ByteArray -> {
            intent.putExtra(arg.first, value)
          }
          is FloatArray -> {
            intent.putExtra(arg.first, value)
          }
          is DoubleArray -> {
            intent.putExtra(arg.first, value)
          }
          is BooleanArray -> {
            intent.putExtra(arg.first, value)
          }
          is Serializable -> {
            intent.putExtra(arg.first, value)
          }
          is LongArray -> {
            intent.putExtra(arg.first, value)
          }
          is CharSequence -> {
            intent.putExtra(arg.first, value)
          }
        }
      }
      manager.sendBroadcast(intent)
    }

  }
}


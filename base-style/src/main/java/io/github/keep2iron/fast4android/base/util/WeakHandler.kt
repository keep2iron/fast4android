/*
 * Create bt Keep2iron on 17-7-1 下午6:38
 */

package io.github.keep2iron.fast4android.base.util

import android.os.Handler
import android.os.Handler.Callback
import android.os.Looper
import android.os.Message
import androidx.annotation.VisibleForTesting
import java.lang.ref.WeakReference
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class WeakHandler {
  /**
   * hard reference to Callback. We need to keep callback in memory
   */
  private var mCallback: Callback? = null
  private var mExec: ExecHandler
  private val mLock = ReentrantLock()
  @VisibleForTesting
  internal val mRunnables =
    ChainedRef(mLock, null)

  val looper: Looper
    get() = mExec.looper

  /**
   * Default constructor associates this handler with the [Looper] for the
   * current thread.
   *
   *
   * If this thread does not have a looper, this handler won't be able to receive messages
   * so an exception is thrown.
   */
  constructor() {
    mCallback = null
    mExec = ExecHandler()
  }

  /**
   * Constructor associates this handler with the [Looper] for the
   * current thread and takes a callback interface in which you can handle
   * messages.
   *
   *
   * If this thread does not have a looper, this handler won't be able to receive messages
   * so an exception is thrown.
   *
   * @param callback The callback interface in which to handle messages, or null.
   */
  constructor(callback: Callback?) {
    mCallback = callback // Hard referencing body
    mExec =
      ExecHandler(WeakReference<Callback>(callback)) // Weak referencing inside ExecHandler
  }

  /**
   * Use the provided [Looper] instead of the default one.
   *
   * @param looper The looper, must not be null.
   */
  constructor(looper: Looper) {
    mCallback = null
    mExec = ExecHandler(looper)
  }

  /**
   * Use the provided [Looper] instead of the default one and take a callback
   * interface in which to handle messages.
   *
   * @param looper The looper, must not be null.
   * @param callback The callback interface in which to handle messages, or null.
   */
  constructor(looper: Looper, callback: Callback) {
    mCallback = callback
    mExec =
      ExecHandler(looper, WeakReference(callback))
  }

  /**
   * Causes the Runnable r to be added to the message queue.
   * The runnable will be run on the thread to which this handler is
   * attached.
   *
   * @param r The Runnable that will be executed.
   * @return Returns true if the Runnable was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   */
  fun post(r: Runnable): Boolean {
    return mExec.post(wrapRunnable(r))
  }

  /**
   * Causes the Runnable r to be added to the message queue, to be run
   * at a specific time given by <var>uptimeMillis</var>.
   * **The time-base is [android.os.SystemClock.uptimeMillis].**
   * The runnable will be run on the thread to which this handler is attached.
   *
   * @param r The Runnable that will be executed.
   * @param uptimeMillis The absolute time at which the callback should run,
   * using the [android.os.SystemClock.uptimeMillis] time-base.
   * @return Returns true if the Runnable was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.  Note that a
   * result of true does not mean the Runnable will be processed -- if
   * the looper is quit before the delivery time of the message
   * occurs then the message will be dropped.
   */
  fun postAtTime(r: Runnable, uptimeMillis: Long): Boolean {
    return mExec.postAtTime(wrapRunnable(r), uptimeMillis)
  }

  /**
   * Causes the Runnable r to be added to the message queue, to be run
   * at a specific time given by <var>uptimeMillis</var>.
   * **The time-base is [android.os.SystemClock.uptimeMillis].**
   * The runnable will be run on the thread to which this handler is attached.
   *
   * @param r The Runnable that will be executed.
   * @param uptimeMillis The absolute time at which the callback should run,
   * using the [android.os.SystemClock.uptimeMillis] time-base.
   * @return Returns true if the Runnable was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.  Note that a
   * result of true does not mean the Runnable will be processed -- if
   * the looper is quit before the delivery time of the message
   * occurs then the message will be dropped.
   * @see android.os.SystemClock.uptimeMillis
   */
  fun postAtTime(r: Runnable, token: Any, uptimeMillis: Long): Boolean {
    return mExec.postAtTime(wrapRunnable(r), token, uptimeMillis)
  }

  /**
   * Causes the Runnable r to be added to the message queue, to be run
   * after the specified amount of time elapses.
   * The runnable will be run on the thread to which this handler
   * is attached.
   *
   * @param r The Runnable that will be executed.
   * @param delayMillis The delay (in milliseconds) until the Runnable
   * will be executed.
   * @return Returns true if the Runnable was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.  Note that a
   * result of true does not mean the Runnable will be processed --
   * if the looper is quit before the delivery time of the message
   * occurs then the message will be dropped.
   */
  fun postDelayed(r: Runnable, delayMillis: Long): Boolean {
    return mExec.postDelayed(wrapRunnable(r), delayMillis)
  }

  /**
   * Posts a message to an object that implements Runnable.
   * Causes the Runnable r to executed on the next iteration through the
   * message queue. The runnable will be run on the thread to which this
   * handler is attached.
   * **This method is only for use in very special circumstances -- it
   * can easily starve the message queue, cause ordering problems, or have
   * other unexpected side-effects.**
   *
   * @param r The Runnable that will be executed.
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   */
  fun postAtFrontOfQueue(r: Runnable): Boolean {
    return mExec.postAtFrontOfQueue(wrapRunnable(r))
  }

  /**
   * Remove any pending posts of Runnable r that are in the message queue.
   */
  fun removeCallbacks(r: Runnable) {
    val runnable = mRunnables.remove(r)
    if (runnable != null) {
      mExec.removeCallbacks(runnable)
    }
  }

  /**
   * Remove any pending posts of Runnable <var>r</var> with Object
   * <var>token</var> that are in the message queue.  If <var>token</var> is null,
   * all callbacks will be removed.
   */
  fun removeCallbacks(r: Runnable, token: Any) {
    val runnable = mRunnables.remove(r)
    if (runnable != null) {
      mExec.removeCallbacks(runnable, token)
    }
  }

  /**
   * Pushes a message onto the end of the message queue after all pending messages
   * before the current time. It will be received in callback,
   * in the thread attached to this handler.
   *
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   */
  fun sendMessage(msg: Message): Boolean {
    return mExec.sendMessage(msg)
  }

  /**
   * Sends a Message containing only the what value.
   *
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   */
  fun sendEmptyMessage(what: Int): Boolean {
    return mExec.sendEmptyMessage(what)
  }

  /**
   * Sends a Message containing only the what value, to be delivered
   * after the specified amount of time elapses.
   *
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   * @see .sendMessageDelayed
   */
  fun sendEmptyMessageDelayed(what: Int, delayMillis: Long): Boolean {
    return mExec.sendEmptyMessageDelayed(what, delayMillis)
  }

  /**
   * Sends a Message containing only the what value, to be delivered
   * at a specific time.
   *
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   * @see .sendMessageAtTime
   */
  fun sendEmptyMessageAtTime(what: Int, uptimeMillis: Long): Boolean {
    return mExec.sendEmptyMessageAtTime(what, uptimeMillis)
  }

  /**
   * Enqueue a message into the message queue after all pending messages
   * before (current time + delayMillis). You will receive it in
   * callback, in the thread attached to this handler.
   *
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.  Note that a
   * result of true does not mean the message will be processed -- if
   * the looper is quit before the delivery time of the message
   * occurs then the message will be dropped.
   */
  fun sendMessageDelayed(msg: Message, delayMillis: Long): Boolean {
    return mExec.sendMessageDelayed(msg, delayMillis)
  }

  /**
   * Enqueue a message into the message queue after all pending messages
   * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
   * **The time-base is [android.os.SystemClock.uptimeMillis].**
   * You will receive it in callback, in the thread attached
   * to this handler.
   *
   * @param uptimeMillis The absolute time at which the message should be
   * delivered, using the
   * [android.os.SystemClock.uptimeMillis] time-base.
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.  Note that a
   * result of true does not mean the message will be processed -- if
   * the looper is quit before the delivery time of the message
   * occurs then the message will be dropped.
   */
  fun sendMessageAtTime(msg: Message, uptimeMillis: Long): Boolean {
    return mExec.sendMessageAtTime(msg, uptimeMillis)
  }

  /**
   * Enqueue a message at the front of the message queue, to be processed on
   * the next iteration of the message loop.  You will receive it in
   * callback, in the thread attached to this handler.
   * **This method is only for use in very special circumstances -- it
   * can easily starve the message queue, cause ordering problems, or have
   * other unexpected side-effects.**
   *
   * @return Returns true if the message was successfully placed in to the
   * message queue.  Returns false on failure, usually because the
   * looper processing the message queue is exiting.
   */
  fun sendMessageAtFrontOfQueue(msg: Message): Boolean {
    return mExec.sendMessageAtFrontOfQueue(msg)
  }

  /**
   * Remove any pending posts of messages with code 'what' that are in the
   * message queue.
   */
  fun removeMessages(what: Int) {
    mExec.removeMessages(what)
  }

  /**
   * Remove any pending posts of messages with code 'what' and whose obj is
   * 'object' that are in the message queue.  If <var>object</var> is null,
   * all messages will be removed.
   */
  fun removeMessages(what: Int, `object`: Any) {
    mExec.removeMessages(what, `object`)
  }

  /**
   * Remove any pending posts of callbacks and sent messages whose
   * <var>obj</var> is <var>token</var>.  If <var>token</var> is null,
   * all callbacks and messages will be removed.
   */
  fun removeCallbacksAndMessages(token: Any) {
    mExec.removeCallbacksAndMessages(token)
  }

  /**
   * Check if there are any pending posts of messages with code 'what' in
   * the message queue.
   */
  fun hasMessages(what: Int): Boolean {
    return mExec.hasMessages(what)
  }

  /**
   * Check if there are any pending posts of messages with code 'what' and
   * whose obj is 'object' in the message queue.
   */
  fun hasMessages(what: Int, `object`: Any): Boolean {
    return mExec.hasMessages(what, `object`)
  }

  private fun wrapRunnable(r: Runnable): WeakRunnable {
    val hardRef = ChainedRef(mLock, r)
    mRunnables.insertAfter(hardRef)
    return hardRef.wrapper
  }

  class ExecHandler : Handler {
    private val mCallback: WeakReference<Callback>?

    constructor() {
      mCallback = null
    }

    constructor(callback: WeakReference<Callback>) : super() {
      mCallback = callback
    }

    constructor(looper: Looper) : super(looper) {
      mCallback = null
    }

    constructor(
      looper: Looper,
      callback: WeakReference<Callback>
    ) : super(looper) {
      mCallback = callback
    }

    override fun handleMessage(msg: Message) {
      if (mCallback == null) {
        return
      }
      val callback = mCallback.get()
        ?: // Already disposed
        return
      callback.handleMessage(msg)
    }
  }

  class WeakRunnable(
    private val mDelegate: WeakReference<Runnable>,
    private val mReference: WeakReference<ChainedRef>
  ) : Runnable {

    override fun run() {
      val delegate = mDelegate.get()
      val reference = mReference.get()
      reference?.remove()
      delegate?.run()
    }
  }

  class ChainedRef(var lock: Lock, val runnable: Runnable?) {
    var next: ChainedRef? = null
    var prev: ChainedRef? = null
    val wrapper: WeakRunnable =
      WeakRunnable(
        WeakReference<Runnable>(runnable),
        WeakReference<ChainedRef>(this)
      )

    fun remove(): WeakRunnable {
      lock.lock()
      try {
        if (prev != null) {
          prev!!.next = next
        }
        if (next != null) {
          next!!.prev = prev
        }
        prev = null
        next = null
      } finally {
        lock.unlock()
      }
      return wrapper
    }

    fun insertAfter(candidate: ChainedRef) {
      lock.lock()
      try {
        if (this.next != null) {
          this.next!!.prev = candidate
        }

        candidate.next = this.next
        this.next = candidate
        candidate.prev = this
      } finally {
        lock.unlock()
      }
    }

    fun remove(obj: Runnable): WeakRunnable? {
      lock.lock()
      try {
        var curr = this.next // Skipping head
        while (curr != null) {
          if (curr.runnable === obj) { // We do comparison exactly how Handler does inside
            return curr.remove()
          }
          curr = curr.next
        }
      } finally {
        lock.unlock()
      }
      return null
    }
  }
}
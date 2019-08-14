package io.github.keep2iron.app

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.reactivestreams.Subscription
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  var subscribe: Disposable? = null

  @Test
  fun addition_isCorrect() {
//        val observable = Observable.create(ObservableOnSubscribe<Any> {
//            System.out.println("before......")
//            Thread.sleep(3000)
//            System.out.println("after......")
//            throw RuntimeException("error....")
//        }).delay(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                    System.out.println("on next......")
//                })
//        System.out.println("dispose before......")
//        observable.dispose()
//        System.out.println("dispose after......")

//        Single.timer(2, TimeUnit.SECONDS)
//                .subscribe({ _ -> throw RuntimeException() })

    val values = Observable.create(ObservableOnSubscribe<Any> { it ->
      //            System.out.println("${it.isDisposed}")
      it.onNext("123456")
//            it.onError(RuntimeException("123456"))
//            System.out.println("${subscribe?.dispose()}")
      subscribe?.dispose()
      System.out.println("${it.isDisposed}")
      it.onError(RuntimeException("123456"))
    })
//
    values?.subscribe(
      {
        System.out.println("successful")
      },
      { e ->
        System.out.println("Error: " + e.message)
      },
      {
        System.out.println("Completed")
      },
      { t: Disposable? ->
        subscribe = t
      }
    )
  }

  @Test
  fun test2() {
    val poolExecutor = ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, ArrayBlockingQueue(10))

    var subscription: Subscription? = null
    subscribe = Flowable.create(FlowableOnSubscribe<String> { e ->
      e.onNext("11")
      subscription?.cancel()
//                subscribe?.dispose()
//            e.onError(RuntimeException("11111error"))
      poolExecutor.shutdownNow()
      poolExecutor.execute {
        System.out.println("执行一段代码")
      }
    }, BackpressureStrategy.ERROR)/*.delay(1000,TimeUnit.MILLISECONDS)*/
      //.take(1)
      .subscribeOn(Schedulers.io())
      .subscribe({
      }, {
        System.out.println("Error: ${it.message}")
      }, {

      }, {
        subscription = it
      })
  }

  @Test
  fun testReduce() {
    val reduce = arrayListOf(1, 2, 3, 4).reduce { v1, v2 ->
      return@reduce v1 + v2
    }
    System.out.println("reduce $reduce")
  }

  @Test
  fun testRange() {
    System.out.println(10 downTo 1)
  }
}

package io.github.keep2iron.fast4android.ext

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.ioAsyncScheduler(): Observable<T> {
  return observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
}

fun <T> Flowable<T>.ioAsyncScheduler(): Flowable<T> {
  return observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
}
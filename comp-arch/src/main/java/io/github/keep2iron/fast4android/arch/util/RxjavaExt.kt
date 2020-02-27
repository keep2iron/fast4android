package io.github.keep2iron.fast4android.arch.util

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
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

fun <T> Single<T>.ioAsyncScheduler(): Single<T> {
  return observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
}

fun Completable.ioAsyncScheduler(): Completable {
  return observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
}
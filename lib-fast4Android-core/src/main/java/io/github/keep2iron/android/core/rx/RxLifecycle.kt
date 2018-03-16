/*
 * Create bt Keep2iron on 17-6-26 下午4:27
 * Copyright (c) 2017. All rights reserved.
 */

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.keep2iron.android.core.rx

import io.github.keep2iron.android.util.Preconditions
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function

class RxLifecycle private constructor() {

    init {
        throw AssertionError("No instances")
    }

    companion object {

        /**
         * Binds the given source to a lifecycle.
         *
         *
         * When the lifecycle event occurs, the source will cease to emit any notifications.
         *
         * @param lifecycle the lifecycle sequence
         * @param event the event which should conclude notifications from the source
         * @return a reusable [LifecycleTransformer] that unsubscribes the source at the specified event
         */
        fun <T, R> bindUntilEvent(lifecycle: Observable<R>,
                                  event: R): LifecycleTransformer<T> {
            Preconditions.checkNotNull(lifecycle, "lifecycle == null")
            Preconditions.checkNotNull(event, "event == null")
            return bind(takeUntilEvent(lifecycle, event))
        }

        private fun <R> takeUntilEvent(lifecycle: Observable<R>, event: R): Observable<R> {
            return lifecycle.filter { lifecycleEvent -> lifecycleEvent == event }
        }

        /**
         * Binds the given source to a lifecycle.
         *
         *
         * This helper automatically determines (based on the lifecycle sequence itself) when the source
         * should stop emitting items. Note that for this method, it assumes *any* event
         * emitted by the given lifecycle indicates that the lifecycle is over.
         *
         * @param lifecycle the lifecycle sequence
         * @return a reusable [LifecycleTransformer] that unsubscribes the source whenever the lifecycle emits
         */
        fun <T, R> bind(lifecycle: Observable<R>): LifecycleTransformer<T> {
            return LifecycleTransformer(lifecycle)
        }

        /**
         * Binds the given source to a lifecycle.
         *
         *
         * This method determines (based on the lifecycle sequence itself) when the source
         * should stop emitting items. It uses the provided correspondingEvents function to determine
         * when to unsubscribe.
         *
         *
         * Note that this is an advanced usage of the library and should generally be used only if you
         * really know what you're doing with a given lifecycle.
         *
         * @param lifecycle the lifecycle sequence
         * @param correspondingEvents a function which tells the source when to unsubscribe
         * @return a reusable [LifecycleTransformer] that unsubscribes the source during the Fragment lifecycle
         */
        fun <T, R> bind(lifecycle: Observable<R>,
                        correspondingEvents: Function<R, R>): LifecycleTransformer<T> {
            Preconditions.checkNotNull(lifecycle, "lifecycle == null")
            Preconditions.checkNotNull(correspondingEvents, "correspondingEvents == null")
            return bind(takeUntilCorrespondingEvent(lifecycle.share(), correspondingEvents))
        }

        private fun <R> takeUntilCorrespondingEvent(lifecycle: Observable<R>,
                                                    correspondingEvents: Function<R, R>): Observable<Boolean> {
            return Observable.combineLatest(
                    lifecycle.take(1).map(correspondingEvents),
                    lifecycle.skip(1),
                    BiFunction<R, R, Boolean> { bindUntilEvent, lifecycleEvent -> lifecycleEvent == bindUntilEvent })
                    .onErrorReturn(Functions.RESUME_FUNCTION)
                    .filter(Functions.SHOULD_COMPLETE)
        }
    }
}
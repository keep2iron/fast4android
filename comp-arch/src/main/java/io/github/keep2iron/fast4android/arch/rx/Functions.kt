/*
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

package io.github.keep2iron.fast4android.arch.rx

import io.reactivex.Completable
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import java.util.concurrent.CancellationException

internal class Functions private constructor() {

  init {
    throw AssertionError("No instances!")
  }

  companion object {

    val RESUME_FUNCTION: Function<Throwable, Boolean> = Function { throwable ->
      if (throwable is OutsideLifecycleException) {
        return@Function true
      }
      Exceptions.propagate(throwable)
      false
    }

    val SHOULD_COMPLETE: Predicate<Boolean> = Predicate { shouldComplete -> shouldComplete }

    val CANCEL_COMPLETABLE: Function<Any, Completable> =
      Function { Completable.error(CancellationException()) }
  }
}
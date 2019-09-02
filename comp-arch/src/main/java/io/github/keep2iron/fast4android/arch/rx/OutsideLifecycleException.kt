/*
 * Create bt Keep2iron on 17-6-26 下午4:27
 * Copyright (c) 2017. All rights reserved.
 */

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

/**
 * This is an exception that can be thrown to indicate that the caller has attempted to bind to a lifecycle outside
 * of its allowable window.
 */
class OutsideLifecycleException(detailMessage: String?) : IllegalStateException(detailMessage)
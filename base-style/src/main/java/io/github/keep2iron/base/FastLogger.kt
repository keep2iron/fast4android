/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.keep2iron.base

/**
 *
 * @author cginechen
 * @date 2016-08-11
 */
object FastLogger {

    private var sDelegate: FastLogDelegate? = null

    interface FastLogDelegate {
        fun e(tag: String, msg: String, vararg obj: Any)
        fun w(tag: String, msg: String, vararg obj: Any)
        fun i(tag: String, msg: String, vararg obj: Any)
        fun d(tag: String, msg: String, vararg obj: Any)
        fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any)
    }

    fun setDelegate(delegate: FastLogDelegate) {
        sDelegate = delegate
    }

    fun e(tag: String, msg: String, vararg obj: Any) {
        sDelegate?.e(tag, msg, *obj)
    }

    fun w(tag: String, msg: String, vararg obj: Any) {
        sDelegate?.w(tag, msg, *obj)
    }

    fun i(tag: String, msg: String, vararg obj: Any) {
        sDelegate?.i(tag, msg, *obj)
    }

    fun d(tag: String, msg: String, vararg obj: Any) {
        sDelegate?.d(tag, msg, *obj)
    }

    fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any) {
        sDelegate?.printErrStackTrace(tag, tr, format, *obj)
    }
}

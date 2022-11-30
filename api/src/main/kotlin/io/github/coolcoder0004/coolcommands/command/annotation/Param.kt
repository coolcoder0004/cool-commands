/*
 *    Copyright 2022 CoolCoder4
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package io.github.coolcoder0004.coolcommands.command.annotation

import io.github.coolcoder0004.coolcommands.annotation.NoWhitespace
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@MustBeDocumented
@Target(VALUE_PARAMETER)
public annotation class Param(
    @NoWhitespace public val name: String = "",
    public val required: Boolean = DEFAULT_REQUIRED,
    public val infinite: Boolean = DEFAULT_INFINITE
) {

    public companion object {
        public const val DEFAULT_REQUIRED = true
        public const val DEFAULT_INFINITE = false
    }
}

fun Param?.requiredOrDefault() = this?.required ?: Param.DEFAULT_REQUIRED

fun Param?.infiniteOrDefault() = this?.infinite ?: Param.DEFAULT_INFINITE

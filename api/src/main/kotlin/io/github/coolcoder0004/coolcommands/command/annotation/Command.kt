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
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

/**
 * Used to add meta info to a class (parent command) or function (child command).
 */
@MustBeDocumented
@Target(CLASS, FUNCTION)
public annotation class Command(
    @NoWhitespace public val name: String,
    public val description: String = "",
    public val permission: String = "",
    public val usage: String = "",
    public val aliases: Array<@NoWhitespace String> = [],
) {

    public annotation class Default(
        public val description: String = "",
        public val permission: String = "",
        public val usage: String = ""
    )
}

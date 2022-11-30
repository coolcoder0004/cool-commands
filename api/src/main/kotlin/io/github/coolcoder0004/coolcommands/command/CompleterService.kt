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

package io.github.coolcoder0004.coolcommands.command

import kotlin.reflect.KType

public interface CompleterService {

    public fun complete(type: KType, text: String): List<String>

    public fun complete(type: Class<*>, text: String): List<String>

    public fun completerIsRegistered(type: KType): Boolean

    public fun completerIsRegistered(type: Class<*>): Boolean

    public fun getCompleter(type: KType): Completer?

    public fun getCompleter(type: Class<*>): Completer?

    public fun registerCompleter(type: KType, completer: Completer)

    public fun registerCompleter(type: Class<*>, completer: Completer)

    public fun unregisterCompleter(type: KType): Completer?

    public fun unregisterCompleter(type: Class<*>): Completer?
}

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
import kotlin.reflect.full.createType
import kotlin.reflect.full.withNullability

public class CompleterServiceImpl : CompleterService {

    private val registeredCompleters = hashMapOf<KType, Completer>()

    public override fun complete(type: KType, text: String): List<String> {
        val completer = getCompleter(type)
            ?: throw IllegalArgumentException("Converter with type \"$type\" not found")
        return completer.complete(text)
    }

    public override fun complete(type: Class<*>, text: String): List<String> = complete(type.kotlin.createType(), text)

    public override fun completerIsRegistered(type: KType) =
        registeredCompleters.containsKey(type.withNullability(false))

    public override fun completerIsRegistered(type: Class<*>): Boolean = completerIsRegistered(type.kotlin.createType())

    public override fun getCompleter(type: KType): Completer? =
        registeredCompleters[type.withNullability(false)]

    public override fun getCompleter(type: Class<*>): Completer? = getCompleter(type.kotlin.createType())

    public override fun registerCompleter(type: KType, completer: Completer) {
        if (type == Unit::class)
            throw IllegalArgumentException("kClass cannot be void")
        if (completerIsRegistered(type))
            throw IllegalStateException("A converter of that type has already been registered")
        registeredCompleters[type.withNullability(false)] = completer
    }

    public override fun registerCompleter(type: Class<*>, completer: Completer): Unit = registerCompleter(type.kotlin.createType(), completer)

    public override fun unregisterCompleter(type: KType): Completer? =
        registeredCompleters.remove(type.withNullability(false))

    public override fun unregisterCompleter(type: Class<*>): Completer? = unregisterCompleter(type.kotlin.createType())
}

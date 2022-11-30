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

public class ConverterServiceImpl : ConverterService {
    private val converters = hashMapOf<KType, Converter<out Any>>()

    public override fun convert(string: String, type: KType): Any? {
        val converter = getConverter(type)
            ?: throw IllegalArgumentException("Converter with type \"$type\" not found")
        return converter.convert(string)
    }

    public override fun convert(string: String, type: Class<*>): Any? = convert(string, type.kotlin.createType())

    public override fun converterIsRegistered(type: KType): Boolean =
        converters.containsKey(type.withNullability(false))

    public override fun converterIsRegistered(type: Class<*>): Boolean = converterIsRegistered(type.kotlin.createType())

    public override fun getConverter(type: KType) =
        converters[type.withNullability(false)]

    public override fun getConverter(type: Class<*>): Converter<out Any>? = getConverter(type.kotlin.createType())

    public override fun registerConverter(type: KType, converter: Converter<out Any>) {
        if (type == Unit::class)
            throw IllegalArgumentException("kClass cannot be void")
        if (converterIsRegistered(type))
            throw IllegalStateException("A converter of that type has already been registered")

        converters[type.withNullability(false)] = converter
    }

    public override fun registerConverter(type: Class<*>, converter: Converter<out Any>) =
        registerConverter(type.kotlin.createType(), converter)

    public override fun unregisterConverter(type: KType): Converter<out Any>? =
        converters.remove(type.withNullability(false))

    public override fun unregisterConverter(type: Class<*>): Converter<out Any>? =
        unregisterConverter(type.kotlin.createType())
}

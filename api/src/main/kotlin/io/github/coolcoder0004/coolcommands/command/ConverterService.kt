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

public interface ConverterService {

    /**
     * Converts a string to specified type. If the conversion fails because
     * the input is invalid, `null` is returned.
     *
     * @param string the string to convert.
     * @param type the type to convert to.
     * @return the converted string.
     * @throws IllegalArgumentException if converter with key [type] was not found.
     */
    public fun convert(string: String, type: KType): Any?

    public fun convert(string: String, type: Class<*>): Any?

    /**
     * Checks if the converter of type [type] exists.
     *
     * @param type the type of the converter.
     * @return `true` if the converter exists, otherwise `false`.
     */
    public fun converterIsRegistered(type: KType): Boolean

    public fun converterIsRegistered(type: Class<*>): Boolean

    /**
     * Gets a converter by type.
     *
     * @param type the type of the converter.
     * @return the converter.
     */
    public fun getConverter(type: KType): Converter<out Any>?

    public fun getConverter(type: Class<*>): Converter<out Any>?

    /**
     * @throws IllegalArgumentException if kClass is not a type. e.g. `Unit`.
     * @throws IllegalStateException if converter of type [type] has already
     * been registered.
     */
    public fun registerConverter(type: KType, converter: Converter<out Any>)

    public fun registerConverter(type: Class<*>, converter: Converter<out Any>)

    /**
     * Unregisters converter of type [type].
     *
     * @param type the type of the converter.
     * @return the previous converter or `null` if there was no previous
     * converter.
     */
    public fun unregisterConverter(type: KType): Converter<out Any>?

    public fun unregisterConverter(type: Class<*>): Converter<out Any>?
}

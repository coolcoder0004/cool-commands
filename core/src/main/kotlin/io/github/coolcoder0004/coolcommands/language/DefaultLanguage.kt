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

package io.github.coolcoder0004.coolcommands.language

import io.github.coolcoder0004.coolcommands.annotation.ObjectPath

public class DefaultLanguage(languageTag: String) : AbstractLanguage(languageTag) {

    private val messages = mapOf(
        DefaultMessages.NO_PERMISSION to "No permission. ",
        DefaultMessages.INVALID_USAGE to "Invalid usage. Try: ",
        DefaultMessages.SERVER_ERROR to "Internal error. Please try again later. ",
        DefaultMessages.USER_ERROR to "Error: ",
        DefaultMessages.NOT_FOUND to "Command not found. ",
        DefaultMessages.INVALID_ARGUMENT to "Invalid argument. ",
        DefaultMessages.CANNOT_EXECUTE to "You may not execute this command. "
    )

    override fun getMessage(@ObjectPath key: String) = messages.getOrDefault(key, key)
}

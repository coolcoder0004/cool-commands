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

/**
 * Represents a speakable language.
 */
public interface Language {

    /**
     * RFC 5646 language tag.
     */
    public val languageTag: String

    /**
     * Gets message by key. If a message with specified key does not exist,
     * the key should be returned.
     *
     * @param key the message key.
     * @return the message.
     */
    public fun getMessage(@ObjectPath key: String): String
}

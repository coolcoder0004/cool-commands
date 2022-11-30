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

import io.github.coolcoder0004.coolcommands.annotation.ObjectPath
import io.github.coolcoder0004.coolcommands.language.Language
import io.github.coolcoder0004.coolcommands.templates.TemplateInjector

public class TemplateInjectorImpl : TemplateInjector {
    private val regex = Regex("""\{\{${ObjectPath.PATTERN}}}""")
    public override fun inject(language: Language, string: String, recursive: Boolean): String =
        regex.replace(string) {
            val value = it.value
            val message = language.getMessage(value.substring(2, value.length - 2))
            if (!recursive)
                message
            else
                inject(language, message, true)
        }
}

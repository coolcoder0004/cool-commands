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

import io.github.coolcoder0004.coolcommands.language.LanguageService
import io.github.coolcoder0004.coolcommands.templates.TemplateInjector

public interface CommandManager {

    public val registeredCommands: Map<String, BaseCommand>
    public val converterService: ConverterService
    public val completerService: CompleterService
    public val languageService: LanguageService
    public val templateInjector: TemplateInjector
    public val senderMapper: SenderMapper

    public fun registerCommand(command: Any): BaseCommand

    public fun registerCommand(command: BaseCommand)

    public fun getCommand(label: String): BaseCommand?

    public fun execute(sender: Sender, alias: String, args: List<String>)

    public fun getCompletions(sender: Sender, alias: String, args: List<String>): List<String>

    public interface Builder {
        public fun converterService(converterService: ConverterService): Builder

        public fun completerService(completerService: CompleterService): Builder

        public fun languageService(languageService: LanguageService): Builder

        public fun templateInjector(templateInjector: TemplateInjector): Builder

        public fun senderMapper(senderMapper: SenderMapper): Builder

        public fun build(): CommandManager
    }
}

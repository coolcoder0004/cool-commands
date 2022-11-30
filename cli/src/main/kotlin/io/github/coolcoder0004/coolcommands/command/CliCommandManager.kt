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

import io.github.coolcoder0004.coolcommands.language.DefaultLanguage
import io.github.coolcoder0004.coolcommands.language.DefaultLanguageService
import io.github.coolcoder0004.coolcommands.language.LanguageService
import io.github.coolcoder0004.coolcommands.templates.TemplateInjector

public class CliCommandManager private constructor(
    converterService: ConverterService,
    completerService: CompleterService,
    languageService: LanguageService,
    templateInjector: TemplateInjector,
    senderMapper: SenderMapper
) : AbstractCommandManager(
    converterService,
    completerService,
    languageService,
    templateInjector,
    senderMapper
) {

    public fun execute(vararg args: String) {
        if (args.isEmpty()) throw IllegalArgumentException("Args cannot be empty")
        execute(CliSender(templateInjector, languageService), args[0], args.drop(1))
    }

    public class Builder private constructor(
        converterService: ConverterService,
        completerService: CompleterService,
        languageService: LanguageService,
        templateInjector: TemplateInjector,
        senderMapper: SenderMapper
    ) : AbstractBuilder(
        converterService,
        completerService,
        languageService,
        templateInjector,
        senderMapper
    ) {
        override fun build(): CliCommandManager =
            CliCommandManager(converterService, completerService, languageService, templateInjector, senderMapper)

        companion object {
            @JvmSynthetic
            internal fun newBuilder(
                converterService: ConverterService = ConverterServiceImpl(),
                completerService: CompleterService = CompleterServiceImpl(),
                languageService: LanguageService = DefaultLanguageService(DefaultLanguage("en-US")),
                templateInjector: TemplateInjector = TemplateInjectorImpl(),
                senderMapper: SenderMapper = SenderMapper { it }
            ) = Builder(converterService, completerService, languageService, templateInjector, senderMapper)
        }
    }

    public companion object {
        @JvmStatic
        public fun builder(): Builder = Builder.newBuilder()
        @JvmStatic
        public fun create(): CliCommandManager = builder().build()
    }
}

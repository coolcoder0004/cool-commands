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

import io.github.coolcoder0004.coolcommands.command.completer.BooleanCompleter
import io.github.coolcoder0004.coolcommands.command.completer.CharacterCompleter
import io.github.coolcoder0004.coolcommands.command.completer.CommandCompleter
import io.github.coolcoder0004.coolcommands.command.converter.BooleanConverter
import io.github.coolcoder0004.coolcommands.command.converter.ByteConverter
import io.github.coolcoder0004.coolcommands.command.converter.CharacterConverter
import io.github.coolcoder0004.coolcommands.command.converter.CommandConverter
import io.github.coolcoder0004.coolcommands.command.converter.DoubleConverter
import io.github.coolcoder0004.coolcommands.command.converter.FloatConverter
import io.github.coolcoder0004.coolcommands.command.converter.IntConverter
import io.github.coolcoder0004.coolcommands.command.converter.LongConverter
import io.github.coolcoder0004.coolcommands.command.converter.PatternConverter
import io.github.coolcoder0004.coolcommands.command.converter.RegexConverter
import io.github.coolcoder0004.coolcommands.command.converter.ShortConverter
import io.github.coolcoder0004.coolcommands.command.converter.StringConverter
import io.github.coolcoder0004.coolcommands.command.converter.UByteConverter
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.NOT_FOUND
import io.github.coolcoder0004.coolcommands.language.LanguageService
import io.github.coolcoder0004.coolcommands.templates.TemplateInjector
import java.util.regex.Pattern
import kotlin.reflect.typeOf

public abstract class AbstractCommandManager(
    public override val converterService: ConverterService,
    public override val completerService: CompleterService,
    public override val languageService: LanguageService,
    public override val templateInjector: TemplateInjector,
    public override val senderMapper: SenderMapper,
    private val commandMap: CommandMap = CommandMapImpl()
) : CommandManager {

    public override val registeredCommands: Map<String, BaseCommand>
        get() = commandMap.registeredCommands

    init {
        @Suppress("LeakingThis")
        mapOf(
            typeOf<Boolean>() to BooleanConverter(),
            typeOf<Byte>() to ByteConverter(),
            typeOf<Char>() to CharacterConverter(),
            typeOf<BaseCommand>() to CommandConverter(this),
            typeOf<Double>() to DoubleConverter(),
            typeOf<Float>() to FloatConverter(),
            typeOf<Int>() to IntConverter(),
            typeOf<Long>() to LongConverter(),
            typeOf<Pattern>() to PatternConverter(),
            typeOf<Regex>() to RegexConverter(),
            typeOf<Short>() to ShortConverter(),
            typeOf<String>() to StringConverter(),
            typeOf<UByte>() to UByteConverter()
        ).forEach { (type, converter) -> converterService.registerConverter(type, converter) }

        @Suppress("LeakingThis")
        mapOf(
            typeOf<Boolean>() to BooleanCompleter(),
            typeOf<Char>() to CharacterCompleter("abcdefghijklmnopqrstuvwxyz".split("")),
            typeOf<BaseCommand>() to CommandCompleter(this)
        ).forEach { (type, completer) -> completerService.registerCompleter(type, completer) }
    }

    public override fun registerCommand(command: Any): BaseCommand {
        val classCommand = ClassCommand(command, converterService, completerService, senderMapper)
        registerCommand(classCommand)
        return classCommand
    }

    public override fun registerCommand(command: BaseCommand) {
        commandMap.register(command)
    }

    public override fun getCommand(label: String) = commandMap[label]

    public override fun execute(sender: Sender, alias: String, args: List<String>) {
        getCommand(alias)?.execute(sender, args)
            ?: sender.sendTemplatedMessage("{{$NOT_FOUND}}")
    }

    public override fun getCompletions(sender: Sender, alias: String, args: List<String>): List<String> {
        return getCommand(alias)?.getCompletions(sender, args)
            ?: listOf()
    }

    public abstract class AbstractBuilder constructor(
        protected var converterService: ConverterService,
        protected var completerService: CompleterService,
        protected var languageService: LanguageService,
        protected var templateInjector: TemplateInjector,
        protected var senderMapper: SenderMapper
    ) : CommandManager.Builder {
        public override fun converterService(converterService: ConverterService) =
            apply { this.converterService = converterService }

        public override fun languageService(languageService: LanguageService) =
            apply { this.languageService = languageService }

        public override fun completerService(completerService: CompleterService) =
            apply { this.completerService = completerService }

        public override fun templateInjector(templateInjector: TemplateInjector) =
            apply { this.templateInjector = templateInjector }

        public override fun senderMapper(senderMapper: SenderMapper) =
            apply { this.senderMapper = senderMapper }
    }
}

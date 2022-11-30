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

import io.github.coolcoder0004.coolcommands.command.completer.CommandSenderCompleter
import io.github.coolcoder0004.coolcommands.command.completer.MaterialCompleter
import io.github.coolcoder0004.coolcommands.command.completer.PlayerCompleter
import io.github.coolcoder0004.coolcommands.command.converter.CommandSenderConverter
import io.github.coolcoder0004.coolcommands.command.converter.MaterialConverter
import io.github.coolcoder0004.coolcommands.command.converter.PlayerConverter
import io.github.coolcoder0004.coolcommands.command.converter.WorldConverter
import io.github.coolcoder0004.coolcommands.language.DefaultLanguage
import io.github.coolcoder0004.coolcommands.language.DefaultLanguageService
import io.github.coolcoder0004.coolcommands.language.LanguageService
import io.github.coolcoder0004.coolcommands.templates.TemplateInjector
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.reflect.typeOf

public class BukkitCommandManager private constructor(
    private val plugin: Plugin,
    private val commandMap: CommandMap,
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

    init {
        val server = plugin.server

        mapOf(
            typeOf<Material>() to MaterialConverter(),
            typeOf<Player>() to PlayerConverter(server),
            typeOf<CommandSender>() to CommandSenderConverter(server),
            typeOf<World>() to WorldConverter(server)
        ).forEach { (type, converter) -> converterService.registerConverter(type, converter) }

        mapOf(
            typeOf<CommandSender>() to CommandSenderCompleter(server),
            typeOf<Material>() to MaterialCompleter(),
            typeOf<Player>() to PlayerCompleter(server)
        ).forEach { (type, completer) -> completerService.registerCompleter(type, completer) }
    }

    public override fun registerCommand(command: BaseCommand) {
        super.registerCommand(command)

        commandMap.register(
            command.name,
            plugin.name.lowercase(),
            BukkitCommand(command, languageService, templateInjector)
        )
    }

    public class Builder private constructor(
        private val plugin: Plugin,
        private var commandMap: CommandMap,
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

        public fun commandMap(commandMap: CommandMap) = apply { this.commandMap = commandMap }

        public override fun build(): CommandManager = BukkitCommandManager(
            plugin,
            commandMap,
            converterService,
            completerService,
            languageService,
            templateInjector,
            senderMapper
        )

        public companion object {
            @JvmSynthetic
            internal fun newBuilder(
                plugin: Plugin,
                commandMap: CommandMap = with(plugin.server) {
                    this::class.java
                        .getDeclaredMethod("getCommandMap")
                        .invoke(this) as CommandMap
                },
                converterService: ConverterService = ConverterServiceImpl(),
                completerService: CompleterService = CompleterServiceImpl(),
                languageService: LanguageService = DefaultLanguageService(DefaultLanguage("en-US")),
                templateInjector: TemplateInjector = TemplateInjectorImpl(),
                senderMapper: SenderMapper = SenderMapper { (it as? BukkitSender)?.sender }
            ) = Builder(
                plugin,
                commandMap,
                converterService,
                completerService,
                languageService,
                templateInjector,
                senderMapper
            )
            // Workaround for @JvmSynthetic not supporting constructors
        }
    }

    public companion object {
        @JvmStatic
        public fun builder(plugin: Plugin): Builder = Builder.newBuilder(plugin)

        @JvmStatic
        public fun create(plugin: Plugin): CommandManager = builder(plugin).build()
    }
}

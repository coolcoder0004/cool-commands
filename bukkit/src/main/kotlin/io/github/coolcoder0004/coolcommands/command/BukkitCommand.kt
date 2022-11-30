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
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

internal class BukkitCommand(
    private val command: BaseCommand,
    private val languageService: LanguageService,
    private val templateInjector: TemplateInjector
) : Command(
    command.name,
    command.description,
    command.usage,
    command.aliases
) {
    public override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>
    ): Boolean {
        val bukkitSender = BukkitSender(sender, languageService, templateInjector)
        command.execute(bukkitSender, args.toList())
        return true
    }

    public override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String> {
        val bukkitSender = BukkitSender(sender, languageService, templateInjector)
        return command.getCompletions(bukkitSender, args.toList())
            .filter { it.startsWith(args.last()) }
    }
}

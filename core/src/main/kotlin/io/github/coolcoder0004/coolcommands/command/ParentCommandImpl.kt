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

import io.github.coolcoder0004.coolcommands.annotation.NoWhitespace
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.NO_PERMISSION

public open class ParentCommandImpl(
    name: @NoWhitespace String,
    description: String,
    permission: String,
    usage: String,
    aliases: List<@NoWhitespace String> = listOf(),
    children: List<BaseCommand> = listOf(),
    override val default: CommandExecutor?,
    private val commandMap: CommandMap = CommandMapImpl()
) : AbstractCommand(
    name,
    description,
    permission,
    usage,
    aliases
),
    ParentCommand {

    override val children: Map<String, BaseCommand> get() = commandMap.registeredCommands

    init {
        children.forEach { commandMap.register(it) }
    }

    public override fun getChildCommand(alias: String) = commandMap[alias]

    public override fun execute(sender: Sender, args: List<String>) {
        if (!sender.hasPermission(permission))
            return sender.sendTemplatedMessage("{{$NO_PERMISSION}}")

        if (args.isNotEmpty()) {
            val childCommand = getChildCommand(args[0])
            if (childCommand != null)
                return childCommand.execute(sender, args.drop(1))
        }

        default?.execute(sender, args)
    }

    public override fun getCompletions(sender: Sender, args: List<String>): List<String> {
        return if (args.size > 1) {
            getChildCommand(args[0])?.getCompletions(sender, args.drop(1))
                ?: default?.getCompletions(sender, args) ?: listOf()
        } else {
            children.keys.toList() + (default?.getCompletions(sender, args) ?: listOf())
        }
    }
}

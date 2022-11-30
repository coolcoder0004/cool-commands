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

import io.github.coolcoder0004.coolcommands.command.annotation.Command
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

public class CommandImpl(
    public override val name: String,
    public override val aliases: List<String>,
    commandExecutor: CommandExecutor
) : CommandExecutor by commandExecutor, BaseCommand

fun createFunctionCommand(
    command: Any,
    function: KFunction<Unit>,
    converterService: ConverterService,
    completerService: CompleterService,
    senderMapper: SenderMapper,
    commandAnnotation: Command = function.findAnnotation()
        ?: throw IllegalArgumentException("Function must be annotated with @Command")
) = CommandImpl(
    commandAnnotation.name,
    commandAnnotation.aliases.toList(),
    FunctionCommandExecutor(
        command,
        function,
        commandAnnotation.description,
        commandAnnotation.permission,
        commandAnnotation.usage,
        converterService,
        completerService,
        senderMapper
    )
)

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
import io.github.coolcoder0004.coolcommands.command.annotation.Param
import io.github.coolcoder0004.coolcommands.command.annotation.infiniteOrDefault
import io.github.coolcoder0004.coolcommands.command.annotation.requiredOrDefault
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.CANNOT_EXECUTE
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.INVALID_ARGUMENT
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.INVALID_USAGE
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.NO_PERMISSION
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.SERVER_ERROR
import io.github.coolcoder0004.coolcommands.language.DefaultMessages.USER_ERROR
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class FunctionCommandExecutor(
    private val command: Any,
    private val function: KFunction<Unit>,
    description: String,
    permission: String,
    usage: String,
    private val converterService: ConverterService,
    private val completerService: CompleterService,
    private val senderMapper: SenderMapper
) : AbstractCommandExecutor(
    description,
    permission,
    usage
) {

    private val functionParameters get() = function.parameters
    private val commandParameters = function.parameters.drop(MIN_FUNCTION_ARGS)
    private val minArgs =
        commandParameters.count { it.findAnnotation<Param>().requiredOrDefault() }

    public override fun execute(sender: Sender, args: List<String>) {
        if (!sender.hasPermission(permission))
            return sender.sendTemplatedMessage("{{$NO_PERMISSION}}")

        if (args.size < minArgs)
            return sender.sendTemplatedMessage("{{$INVALID_USAGE}}")

        val mappedSender = senderMapper.map(sender)

        // sender is a subclass of first function param
        if (mappedSender == null || !functionParameters[1].type.jvmErasure.isInstance(mappedSender))
            return sender.sendTemplatedMessage("{{$CANNOT_EXECUTE}}")

        val functionArgs = arrayOfNulls<Any?>(functionParameters.size)
        functionArgs[0] = command
        functionArgs[1] = mappedSender

        for ((i, arg) in args.withIndex()) {
            val parameter = commandParameters.getOrNull(i)
                ?: return sender.sendTemplatedMessage("{{$INVALID_USAGE}}")

            fun convert(arg: String) =
                converterService.convert(arg, parameter.type.withNullability(false))
                    ?: throw ConversionException()

            val index = i + MIN_FUNCTION_ARGS

            try {
                if (parameter.findAnnotation<Param>().infiniteOrDefault()) {
                    functionArgs[index] = convert(args.drop(i).joinToString(" "))
                    break
                } else {
                    functionArgs[index] = convert(arg)
                }
            } catch (e: ConversionException) {
                sender.sendTemplatedMessage(e.message ?: "{{$INVALID_ARGUMENT}}")
            }
        }

        try {
            function.isAccessible = true
            function.call(*functionArgs)
        } catch (e: InvocationTargetException) {
            if (e.cause is CommandException)
                sender.sendTemplatedMessage("{{$USER_ERROR}}${e.message}")
            else
                sender.sendTemplatedMessage("{{$SERVER_ERROR}}")
        } finally {
            function.isAccessible = false
        }
    }

    public override fun getCompletions(sender: Sender, args: List<String>): List<String> {
        val index = if (commandParameters.last().findAnnotation<Param>().infiniteOrDefault())
            args.size.coerceAtMost(commandParameters.size)
        else
            args.size

        if (index > commandParameters.size)
            return listOf()

        return try {
            completerService.complete(commandParameters[index - 1].type, args.last())
        } catch (e: IllegalArgumentException) {
            listOf()
        }
    }

    public companion object {
        /**
         * The minimum function arguments, the instance argument and the sender argument.
         *
         * @author CoolCoder0004
         */
        private const val MIN_FUNCTION_ARGS = 2
    }
}

public fun createDefaultCommandExecutor(
    command: Any,
    function: KFunction<Unit>,
    converterService: ConverterService,
    completerService: CompleterService,
    senderMapper: SenderMapper,
    defaultAnnotation: Command.Default = function.findAnnotation()
        ?: throw IllegalArgumentException("Function must be annotated with @Command.Default")
): CommandExecutor =
    FunctionCommandExecutor(
        command,
        function,
        defaultAnnotation.description,
        defaultAnnotation.permission,
        defaultAnnotation.usage,
        converterService,
        completerService,
        senderMapper
    )

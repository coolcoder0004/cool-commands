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
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.typeOf

@Suppress("UNCHECKED_CAST")
public class ClassCommand(
    command: Any,
    converterService: ConverterService,
    completerService: CompleterService,
    senderMapper: SenderMapper,
    kClass: KClass<*> = command::class,
    commandAnnotation: Command = kClass.findAnnotation()
        ?: throw IllegalArgumentException("Class must be annotated with @Command")
) : ParentCommandImpl(
    commandAnnotation.name,
    commandAnnotation.description,
    commandAnnotation.permission,
    commandAnnotation.usage,
    commandAnnotation.aliases.toList(),
    kClass.memberFunctions
        .filter { it.hasAnnotation<Command>() }
        .filter { it.returnType == typeOf<Unit>() }
        .map {
            createFunctionCommand(
                command,
                it as KFunction<Unit>,
                converterService,
                completerService,
                senderMapper
            )
        } + kClass.nestedClasses
        .filter { it.hasAnnotation<Command>() }
        .map {
            ClassCommand(
                it.createInstance(),
                converterService,
                completerService,
                senderMapper
            )
        },
    createDefaultCommandExecutor(
        command,
        kClass.memberFunctions
            .filter { it.hasAnnotation<Command.Default>() }
            .first { it.returnType == typeOf<Unit>() } as KFunction<Unit>,
        converterService,
        completerService,
        senderMapper
    )
)

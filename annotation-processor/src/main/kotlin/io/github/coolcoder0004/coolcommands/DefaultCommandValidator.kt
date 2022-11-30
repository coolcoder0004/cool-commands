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

package io.github.coolcoder0004.coolcommands

import com.google.auto.service.AutoService
import io.github.coolcoder0004.coolcommands.command.annotation.Command
import javax.annotation.processing.Processor
import javax.lang.model.element.Element

@AutoService(Processor::class)
@ValidatesAnnotations(Command::class)
class DefaultCommandValidator : Validator(Only1DefaultCommandValidator())

class Only1DefaultCommandValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        val defaultCommandCount = element.enclosingElement.enclosedElements
            .count { it.hasAnnotation<Command.Default>() }

        if (defaultCommandCount > 1)
            throw CompilationError("Command can only have 1 default command. ")
    }
}

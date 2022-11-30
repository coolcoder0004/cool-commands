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
import io.github.coolcoder0004.coolcommands.annotation.NoWhitespace
import io.github.coolcoder0004.coolcommands.command.annotation.Command
import javax.annotation.processing.Processor
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeKind

// todo: maybe fix the names

@AutoService(Processor::class)
@ValidatesAnnotations(Command::class, Command.Default::class)
class CommandValidator : Validator(
    CommandNameEmptyValidator(),
    CommandNameContainsNoWhitespaceValidator(),
    CommandMustHaveSenderParameterValidator(),
    CommandMustHaveVoidReturnTypeValidator(),
    CommandAliasesMustBeNotConflictWithOtherAliasOrNameValidator()
)

class CommandNameEmptyValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        if (annotation !is Command) return
        if (annotation.name.isEmpty())
            throw CompilationError("Command \"${annotation.name}\" cannot be empty")
    }
}

class CommandNameContainsNoWhitespaceValidator : SubValidator {
    private val regex = Regex(NoWhitespace.PATTERN)
    override fun validate(element: Element, annotation: Annotation) {
        if (annotation !is Command) return

        if (annotation.name.matches(regex))
            throw CompilationError("Command \"${annotation.name}\" cannot contain whitespace")
    }
}

class CommandMustHaveSenderParameterValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        if (element.kind != ElementKind.METHOD) return
        if ((element as ExecutableElement).parameters.size < 1)
            throw CompilationError("Command \"${element.simpleName}\" must have at least 1 argument, the sender")
    }
}

class CommandMustHaveVoidReturnTypeValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        if (element.kind != ElementKind.METHOD) return
        if ((element as ExecutableElement).returnType.kind != TypeKind.VOID)
            throw CompilationError("Command \"${element.simpleName}\" must have void return type")
    }
}

class CommandAliasesMustBeNotConflictWithOtherAliasOrNameValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        if (annotation !is Command) return
        val name = annotation.name
        val aliases = annotation.aliases

        for (alias in aliases) {
            if (aliases.count { it == alias } > 1) {
                throw CompilationError("""Command "$name" has multiple "$alias" aliases""")
            }
        }

        if (aliases.contains(name))
            throw CompilationError("""Command "$name" has alias "$name" defined twice""")
    }
}

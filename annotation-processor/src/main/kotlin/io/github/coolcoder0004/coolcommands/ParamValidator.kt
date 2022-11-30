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
import io.github.coolcoder0004.coolcommands.command.annotation.Param
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import javax.annotation.processing.Processor
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement

@AutoService(Processor::class)
@ValidatesAnnotations(Param::class)
@Enclosed(Command::class, Command.Default::class)
class ParamValidator : Validator(
    SenderArgNotAnnotatedValidator(),
    NoSpacesInParamNameValidator(),
    RequiredParametersBeforeOptionalValidator(),
    LastParameterInfiniteValidator(),
    OptionalCannotBeNotNullValidator(),
    RequiredCannotBeNullableValidator()
)

class NoSpacesInParamNameValidator : SubValidator {
    private val regex = Regex(NoWhitespace.PATTERN)
    override fun validate(element: Element, annotation: Annotation) {
        if (!(annotation as Param).name.matches(regex))
            throw CompilationError("Parameter \"${annotation.name}\" contains illegal character: \" \". ")
    }
}

class RequiredParametersBeforeOptionalValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        val parameters = (element.enclosingElement as ExecutableElement).parameters

        // If the next parameter can be optional or not
        var mustBeOptional = false

        for (i in 1 until parameters.size) {
            val required = parameters[i].getAnnotation(Param::class.java)?.required ?: true

            if (mustBeOptional && required)
                throw CompilationError("Required argument cannot be after an optional argument. ")

            if (!required)
                mustBeOptional = true
        }
    }
}

class OptionalCannotBeNotNullValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        if (!(annotation as Param).required && element.hasAnnotation(NotNull::class.java))
            throw CompilationError("Optional argument cannot be marked as @NotNull. ")
    }
}

class RequiredCannotBeNullableValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        if ((annotation as Param).required && element.hasAnnotation(Nullable::class.java))
            throw CompilationError("Required argument cannot be marked as @Nullable")
    }
}

class SenderArgNotAnnotatedValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        val method = element.enclosingElement as ExecutableElement
        if (method.parameters.indexOf(element) == 0)
            throw CompilationError("Only command parameters can be annotated with \"@${Param::class.simpleName}\" not \"${element.simpleName}\". ")
    }
}

class LastParameterInfiniteValidator : SubValidator {
    override fun validate(element: Element, annotation: Annotation) {
        val parameters = (element.enclosingElement as ExecutableElement).parameters
        val index = parameters.indexOf(element)
        val last = parameters.size - 1

        if (index < last && (annotation as Param).infinite)
            throw CompilationError("Only the last parameter can have an infinite length. Found $index/$last. ")
    }
}

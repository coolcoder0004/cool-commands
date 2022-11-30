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

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.reflect.full.findAnnotation

abstract class Validator(private vararg val subValidators: SubValidator) : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var validatesTypes: Set<Class<out Annotation>>
    private lateinit var enclosed: Set<Class<out Annotation>>

    override fun getSupportedAnnotationTypes() = validatesTypes.map { it.name }.toMutableSet()

    override fun getSupportedSourceVersion() = SourceVersion.latest()!!

    override fun init(processingEnv: ProcessingEnvironment) {
        messager = processingEnv.messager
        validatesTypes = this::class.findAnnotation<ValidatesAnnotations>()
            ?.annotations
            ?.map { it.java }
            ?.toMutableSet()
            ?: mutableSetOf()
        enclosed = this::class.findAnnotation<Enclosed>()
            ?.annotations
            ?.map { it.java }
            ?.toSet()
            ?: setOf()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        for (annotationType in validatesTypes) {
            for (element in roundEnv.getElementsAnnotatedWith(annotationType)) {
                if (enclosed.isEmpty() || element.enclosingElement.hasAnyAnnotations(enclosed))
                    validate(element, element.getAnnotation(annotationType))
            }
        }
        return true
    }

    private fun validate(element: Element, annotation: Annotation) {
        for (subValidator in subValidators) {
            try {
                subValidator.validate(element, annotation)
            } catch (e: CompilationError) {
                messager.printMessage(
                    Diagnostic.Kind.ERROR, e.message, element,
                    element.annotationMirrors.first { it.annotationType.toString() == annotation.annotationClass.qualifiedName }
                )
            }
        }
    }
}

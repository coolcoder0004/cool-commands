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

package io.github.coolcoder0004.coolcommands.annotation

import org.intellij.lang.annotations.Pattern
import kotlin.annotation.AnnotationRetention.SOURCE

@MustBeDocumented
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
@Retention(SOURCE)
@Pattern(ObjectPath.PATTERN)
public annotation class ObjectPath {
    public companion object {
        public const val PATTERN = """[A-z\d-_.$]+"""
    }
}

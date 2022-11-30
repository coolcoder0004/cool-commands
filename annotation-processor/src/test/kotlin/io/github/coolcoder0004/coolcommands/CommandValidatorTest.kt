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

import com.google.testing.compile.Compiler.javac
import com.google.testing.compile.JavaFileObjects
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals

class CommandValidatorTest {

    private val validator = CommandValidator()

    @Test
    fun `Compilation fails if name is invalid`() {

        @Language("java")
        val code = """
            import io.github.coolcoder0004.coolcommands.command.Sender;
            import io.github.coolcoder0004.coolcommands.command.annotation.Command;
            @Command(name = " name")
            class MyCommand {
                @Command(name = "")
                void command(Sender sender) {}
            }
        """.trimIndent()

        val fileObject = JavaFileObjects.forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(2, compilation.errors().size)
    }

    @Test
    fun `Compilation fails if command has no sender argument`() {

        @Language("java")
        val code = """
        import io.github.coolcoder0004.coolcommands.command.annotation.Command;
        @Command(name = "command")
        class MyCommand {
            @Command(name = "command")
            void command() {}
        }
        """.trimIndent()

        val fileObject = JavaFileObjects.forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(1, compilation.errors().size)
    }

    @Test
    fun `Compilation fails if command has return type of anything other than void`() {

        @Language("java")
        val code = """
            import io.github.coolcoder0004.coolcommands.command.annotation.Command;
            import io.github.coolcoder0004.coolcommands.command.Sender;
            @Command(name = "command")
            class MyCommand {
                @Command(name = "command")
                String command(Sender sender) { return ""; }
            }
        """.trimIndent()

        val fileObject = JavaFileObjects.forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(1, compilation.errors().size)
    }
}

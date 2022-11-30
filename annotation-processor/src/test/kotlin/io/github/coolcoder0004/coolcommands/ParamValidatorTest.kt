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
import com.google.testing.compile.JavaFileObjects.forSourceString
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals

class ParamValidatorTest {

    private val validator = ParamValidator()

    @Test
    fun `Compilation fails if infinite parameter is not the last parameter`() {

        @Language("java")
        val code = """
            import io.github.coolcoder0004.coolcommands.command.Sender;
            import io.github.coolcoder0004.coolcommands.command.annotation.Command;
            import io.github.coolcoder0004.coolcommands.command.annotation.Param;
            
            class MyCommand {
                @Command(name="command")
                void command(Sender sender, 
                                       @Param(infinite = true) String s1, 
                                       String s2) {}
            }
        """.trimIndent()

        val fileObject = forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(1, compilation.errors().size)
    }

    @Test
    fun `Compilation fails when sender arg is annotated with @Param`() {

        @Language("java")
        val code = """
            import io.github.coolcoder0004.coolcommands.command.Sender;
            import io.github.coolcoder0004.coolcommands.command.annotation.Command;
            import io.github.coolcoder0004.coolcommands.command.annotation.Param;
            class MyCommand {
                @Command(name="command")
                void command(@Param Sender sender) {}
            }
        """.trimIndent()

        val fileObject = forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(1, compilation.errors().size)
    }

    @Test
    fun `Compilation fails with 1 error when required argument after optional argument`() {

        @Language("java")
        val code = """
            import io.github.coolcoder0004.coolcommands.command.Sender;
            import io.github.coolcoder0004.coolcommands.command.annotation.Command;
            import io.github.coolcoder0004.coolcommands.command.annotation.Param;
            class MyCommand {
                @Command(name="command")
                void command(Sender sender, @Param(required = false) String s1, String s2) {}
            }
        """.trimIndent()

        val fileObject = forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(1, compilation.errors().size)
    }

    @Test
    fun `Compilation succeeds when no issues found`() {

        @Language("java")
        val code = """
            import io.github.coolcoder0004.coolcommands.command.Sender;
            import io.github.coolcoder0004.coolcommands.command.annotation.Command;
            import io.github.coolcoder0004.coolcommands.command.annotation.Param;
            class MyCommand {
                @Command(name="command")
                void command(Sender sender, 
                             @Param(required = false) String s1, 
                             @Param(infinite = true, required = false) String s2) {}
            }
        """.trimIndent()

        val fileObject = forSourceString("io.github.coolcoder0004.coolcommands.MyCommand", code)

        val compilation = javac()
            .withProcessors(validator)
            .compile(fileObject)

        assertEquals(listOf(), compilation.errors())
    }
}

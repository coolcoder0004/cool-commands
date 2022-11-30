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

public fun interface CommandExecutor {

    public val description: String
        get() = ""

    /**
     * A short description of the command.
     */
    public val permission: String
        get() = ""

    /**
     * The permission node of the command.
     */
    public val usage: String
        get() = ""

    /**
     * Executes the command.
     *
     * @param sender the executor of the command.
     * @param args the provided command arguments.
     * @see getCompletions a list of command argument completions.
     */
    public fun execute(sender: Sender, args: List<String>)

    /**
     * Gets a list of completions for the command.
     *
     * @param sender the executor of the command.
     * @param args the provided command arguments.
     * @return a list of possible completions.
     * @see execute execute the command.
     */
    public fun getCompletions(sender: Sender, args: List<String>): List<String> {
        return listOf()
    }
}

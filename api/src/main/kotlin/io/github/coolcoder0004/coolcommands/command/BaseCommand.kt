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

/**
 * A command
 */
public interface BaseCommand : CommandExecutor {

    /**
     * The primary name of the command.
     *
     * @see aliases alternative names.
     */
    public val name: String

    /**
     * Alternative names of the command.
     *
     * @see name the primary name.
     */
    public val aliases: List<String>

    /**
     * A list of all the possible aliases, including the name.
     *
     * @see name the primary name.
     * @see aliases alternative names.
     */
    public val allAliases: List<String> get() = aliases + name
}

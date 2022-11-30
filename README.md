<p style="text-align: center">Cool commands is an annotation based command framework. </p>

<details>
    <summary>Table of Contents</summary>
    <ol>
        <li><a href="#about-the-project">About The Project</a></li>
        <li><a href="#features">Features</a></li>
        <li><a href="#platforms">Platforms</a></li>
    </ol>
</details>

## About The Project

Cool Commands is a command framework made for creating declarative and testable commands

## Features

| Feature                        | cool-commands     | acf | triumph-commands | brigadier |
| ------------------------------ | ----------------- | --- | ---------------- | --------- |
| compilation command validation | ✅                | ❌  | runtime          | N/A       |
| inject custom dependencies     | ✅ (with builder) | ❌  | ❌               | ✅        |
| annotation driven              | ✅                | ✅  | ✅               | ❌        |

## Platforms

These are only platforms that don't require you to call the command handler manually.

| Platform     | cool-commands | acf | triumph-commands | brigadier |
| ------------ | ------------- | --- | ---------------- | --------- |
| CLI          | ✅            | ❌  | ✅               | ❌        |
| bukkit       | ✅            | ✅  | ✅               | ❌        |
| spigot       | ✅            | ✅  | ✅               | ❌        |
| paper        | ✅            | ✅  | ✅               | ❌        |
| bungee       | ❌            | ✅  | ❌               | ❌        |
| JDA prefixed | ❌            | ❌  | ✅               | ❌        |
| JDA slash    | ❌            | ❌  | ✅               | ❌        |

### Links

- Documentation: [Coming soon](#)
- Repository: private

### Basic Usage

1. Clone the repo

2. Build and test the project

```bash
gradlew build publishToMavenLocal
```

3. Add the dependency

```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("io.github.coolcoder0004:coolcommands-bukkit:1.0.0-SNAPSHOT")
}
```

4. Create a basic command

```kotlin
@Command("echo")
class EchoCommand {
    @Command.Default
    fun echo(sender: CommandSender, @Param(infinite = true) text: String) {
        sender.sendMessage(text)
    }
}
```

5. Register the command

```kotlin
val commandManager = BukkitCommandManager.create()
commandManager.registerCommand(EchoCommand())
```

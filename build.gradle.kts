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

plugins {
    kotlin("jvm") version "1.6.20"
    id("java")

    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"

    id("com.github.johnrengelman.shadow") version "4.0.4"

    id("maven-publish")
    id("signing")
}

allprojects {
    group = "io.github.coolcoder0004"
    version = "1.0.0-SNAPSHOT"
    apply {
        apply(plugin = "com.github.johnrengelman.shadow")
        apply(plugin = "java")
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        apply(plugin = "maven-publish")
    }
    java {
        withSourcesJar()
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "${project.group}"
                artifactId = "${rootProject.name}-${project.name}"
                version = "${project.version}"
                from(components["java"])
                pom {
                    name.set("Cool Commands")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("CoolCoder0004")
                            name.set("CoolCoder")
                        }
                    }
                }
            }
        }
        repositories {
            mavenLocal()
        }
    }
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

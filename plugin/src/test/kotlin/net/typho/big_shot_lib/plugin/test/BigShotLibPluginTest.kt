package net.typho.big_shot_lib.plugin.test

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test

class BigShotLibPluginTest {
    @Test
    fun test() {
        val projectDir = createTempDirectory().toFile()

        File(projectDir, "settings.gradle.kts").writeText("")
        File(projectDir, "build.gradle.kts").writeText("""
            plugins {
                id("net.typho.big_shot_lib.plugin")
                kotlin("jvm") version "2.2.0"
            }

            repositories {
                mavenCentral()
            }

            dependencies {
                implementation("org.ow2.asm:asm:9.1")
            }
            
            bigShotLib {
                metadata {
                    modId.set("test_mod")
                }
            }
        """.trimIndent())
        File(projectDir, "src/main/kotlin/Main.kt").apply {
            parentFile.mkdirs()
            writeText("""
                //import org.objectweb.asm.ClassReader

                fun main() {
                    println("this should never run")

                    //val reader = ClassReader("")
                    //reader.test_accept(null, 0)
                }
            """.trimIndent())
        }

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("generateModMetadata")
            .build()

        println(result.output)
    }
}
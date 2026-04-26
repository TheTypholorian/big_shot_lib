plugins {
    kotlin("jvm") version "2.2.0"
    `java-gradle-plugin`
}

group = "net.typho.big_shot_lib"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.ow2.asm:asm:9.1")
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

gradlePlugin {
    plugins {
        create("big_shot_lib_plugin") {
            id = "net.typho.big_shot_lib.plugin"
            implementationClass = "net.typho.big_shot_lib.plugin.BigShotLibPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
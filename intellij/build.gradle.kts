plugins {
    kotlin("jvm") version "2.2.0"
    `java-gradle-plugin`
    id("org.jetbrains.intellij.platform") version "2.17.0"
    `maven-publish`
}

group = "net.typho.big_shot_lib"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
}

kotlin {
    jvmToolchain(21)
}
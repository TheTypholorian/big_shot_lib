plugins {
    kotlin("jvm") version "2.4.0"
}

group = "net.typho.big_shot_lib"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("org.jetbrains:annotations:26.0.2")
    implementation("org.ow2.asm:asm:9.8")
    implementation("org.ow2.asm:asm-tree:9.8")
}

kotlin {
    jvmToolchain(8)
}
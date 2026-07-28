enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// This should match the folder name of the project, or else IDEA may complain (see https://youtrack.jetbrains.com/issue/IDEA-317606)
rootProject.name = "big_shot_lib"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
}

stonecutter {
    create(rootProject) {
        fun match(loader: String, platform: String, vararg versions: String) = versions
            .forEach {
                val versionId = "mc${it.replace('.', '_')}_$loader"
                val propsFile = file("versions/$versionId/gradle.properties")

                if (!propsFile.exists()) {
                    propsFile.parentFile.mkdirs()
                    propsFile.writeText("modstitch.platform=$platform")
                }

                version(versionId, it).buildscript = "build.gradle.kts"
            }

        match("fabric", "fabric-loom-remap", "1.18.2", "1.20.1", "1.21.1")
        match("fabric", "fabric-loom", "26.2")

        //match("forge", "moddevgradle-legacy", "1.18.2", "1.20.1", "1.21.1")
        match("neoforge", "moddevgradle", "26.2")

        //match("fabric", "fabric-loom-remap", /*"1.16.1", "1.16.5", "1.17.1", */"1.18.2", "1.19.2", "1.19.3", "1.19.4", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11")
        //match("fabric", "fabric-loom", "26.1.2", "26.2")
        //match("neoforge", "moddevgradle", /*"1.20.5", */"1.20.6", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11", "26.1.2", "26.2")
        //match("forge", "moddevgradle-legacy", /*"1.16.1", "1.16.5", "1.17.1", */"1.18.2", "1.19.2", "1.19.3", "1.19.4", "1.20.1", "1.20.2", "1.20.3", "1.20.4")

        vcsVersion = "mc26_2_fabric"
    }
}

include("api")
include("agent")
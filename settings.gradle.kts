enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// This should match the folder name of the project, or else IDEA may complain (see https://youtrack.jetbrains.com/issue/IDEA-317606)
rootProject.name = "big_shot_lib"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven {
                    name = "Fabric"
                    url = uri("https://maven.fabricmc.net")
                }
            }
            filter {
                includeGroup("net.fabricmc")
                includeGroup("net.fabricmc.unpick")
                includeGroup("fabric-loom")
            }
        }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        register("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("dev.kikugie.stonecutter") version "0.7-beta.4"
}
include("api")
include("impl")

stonecutter {
    create(project(":impl")) {
        fun match(version: String, vararg loaders: String) = loaders
            .forEach { vers("mc${version.replace(".", "_")}-$it", version).buildscript = "build.$it.gradle.kts" }

        match("1_21_11", "fabric", "neoforge")

        vcsVersion = "mc1_21_11-fabric"
    }
}
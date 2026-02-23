import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("multiloader-loader")
    alias(libs.plugins.moddev)
    id("com.modrinth.minotaur") version "2.+"
}

val modName: String by project
val modId: String by project
val version: String by project

base {
    archivesName = "$modId-mc${libs.versions.minecraft.get()}-neoforge"
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "big-shot-lib"
    versionName = "$modName $version for NeoForge 1.21.8"
    versionNumber = "mc1.21.8-$version-neoforge"
    versionType = "release"
    uploadFile.set(tasks.jar)
    additionalFiles.add(tasks.sourcesJar)
    gameVersions.addAll("1.21.8")
    loaders.add("neoforge")

    dependencies {
        required.project("kotlin-for-forge")
    }
}

neoForge {
    version = libs.versions.neoforge
    // Automatically enable neoforge AccessTransformers if the file exists
    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = libs.versions.parchmentMC
        mappingsVersion = libs.versions.parchment
    }
    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            ideName = "NeoForge ${name.capitalized()} (${project.path})" // Unify the run config names with fabric
        }
        register("client") {
            client()
        }
        register("server") {
            server()
        }
    }
    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

repositories {
    mavenCentral()
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    ivy {
        url = uri("https://github.com/TheTypholorian/big_shot_lib/releases/download")
        patternLayout {
            artifact("[revision]/[artifact]-[revision](-[classifier]).[ext]")
        }
        metadataSources {
            artifact()
        }
    }
}

dependencies {
    implementation(libs.kff)

    fun externalDependency(dependency: Any) {
        jarJar(dependency)
        api(dependency)
        additionalRuntimeClasspath(dependency)
    }

    externalDependency("org.lwjgl:lwjgl-shaderc:3.3.3")

    externalDependency("org.lwjgl:lwjgl-spvc:3.3.3")

    // natives for neoforge are in src/main/resources because neoforge is stupid

    implementation(libs.sodium)

    externalDependency(libs.bigShotApi)
}
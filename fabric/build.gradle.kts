import java.io.FileNotFoundException
import java.nio.file.Files

plugins {
    id("multiloader-loader")
    alias(libs.plugins.loom)
    id("com.modrinth.minotaur") version "2.+"
}

val modName: String by project
val modId: String by project
val version: String by project

base {
    archivesName = "$modId-fabric"
}

modrinth {
    try {
        token = Files.readString(project.rootDir.parentFile.resolve("modrinth_token.txt").toPath())
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    projectId = "big-shot-lib"
    versionName = "$modName $version for Fabric 1.21"
    versionNumber = "mc1.21-$version-fabric"
    versionType = "release"
    uploadFile.set(tasks.remapJar)
    additionalFiles.add(tasks.remapSourcesJar)
    gameVersions.addAll("1.21", "1.21.1")
    loaders.add("fabric")

    dependencies {
        required.project("fabric-language-kotlin")
    }
}

repositories {
    mavenCentral()
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchmentMC.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.fabricLoader)
    modImplementation(libs.fabricApi)

    modImplementation(libs.flk)

    fun lwjglInclude(dependency: Any) {
        include(dependency)
        api(dependency)
    }

    lwjglInclude("org.lwjgl:lwjgl-shaderc:3.3.3")
    lwjglInclude("org.lwjgl:lwjgl-shaderc:3.3.3:natives-windows")
    lwjglInclude("org.lwjgl:lwjgl-shaderc:3.3.3:natives-linux")
    lwjglInclude("org.lwjgl:lwjgl-shaderc:3.3.3:natives-macos")

    lwjglInclude("org.lwjgl:lwjgl-spvc:3.3.3")
    lwjglInclude("org.lwjgl:lwjgl-spvc:3.3.3:natives-windows")
    lwjglInclude("org.lwjgl:lwjgl-spvc:3.3.3:natives-linux")
    lwjglInclude("org.lwjgl:lwjgl-spvc:3.3.3:natives-macos")

    modImplementation(libs.sodium)
}

loom {
    val aw = project(":common").file("src/main/resources/${modId}.accesswidener")
    if (aw.exists()) {
        accessWidenerPath.set(aw)
    }
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }
    runs {
        named("client") {
            client()
            setConfigName("Fabric Client")
            ideConfigGenerated(true)
            runDir("runs/client")
        }
        named("server") {
            server()
            setConfigName("Fabric Server")
            ideConfigGenerated(true)
            runDir("runs/server")
        }
    }
}
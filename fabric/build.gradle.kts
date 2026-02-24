plugins {
    id("multiloader-loader")
    alias(libs.plugins.loom)
    id("com.modrinth.minotaur") version "2.+"
}

val modName: String by project
val modId: String by project
val version: String by project

base {
    archivesName = "$modId-mc${libs.versions.minecraft.get()}-fabric"
}

if (System.getenv("MODRINTH_TOKEN") != null) {
    modrinth {
        token = System.getenv("MODRINTH_TOKEN")
        projectId = "big-shot-lib"
        versionName = "$modName $version for Fabric 1.21.11"
        versionNumber = "mc1.21.11-$version-fabric"
        versionType = "release"
        uploadFile.set(tasks.remapJar)
        additionalFiles.add(tasks.remapSourcesJar)
        gameVersions.addAll("1.21.11")
        loaders.add("fabric")

        dependencies {
            required.project("fabric-language-kotlin")
        }
    }
}

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
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchmentMC.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.fabricLoader)
    modImplementation(libs.fabricApi)

    modImplementation(libs.flk)

    fun externalDependency(dependency: Any) {
        include(dependency)
        api(dependency)
    }

    externalDependency("org.lwjgl:lwjgl-shaderc:3.3.3")
    externalDependency("org.lwjgl:lwjgl-shaderc:3.3.3:natives-windows")
    externalDependency("org.lwjgl:lwjgl-shaderc:3.3.3:natives-linux")
    externalDependency("org.lwjgl:lwjgl-shaderc:3.3.3:natives-macos")

    externalDependency("org.lwjgl:lwjgl-spvc:3.3.3")
    externalDependency("org.lwjgl:lwjgl-spvc:3.3.3:natives-windows")
    externalDependency("org.lwjgl:lwjgl-spvc:3.3.3:natives-linux")
    externalDependency("org.lwjgl:lwjgl-spvc:3.3.3:natives-macos")

    modImplementation(libs.sodium)

    modImplementation(libs.bigShotApi)
    include(libs.bigShotApi) {
        artifact {
            classifier = "intermediary"
        }
    }
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
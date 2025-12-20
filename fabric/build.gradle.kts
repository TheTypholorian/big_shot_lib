plugins {
    id("multiloader-loader")
    alias(libs.plugins.loom)
}

val modId: String by project

repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchmentMC.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.fabricLoader)

    modImplementation(libs.flk)

    fun lwjglInclude(dependency: Any) {
        include(dependency)
        api(dependency)
    }

    lwjglInclude("org.lwjgl:lwjgl-shaderc:3.3.3")
    lwjglInclude("org.lwjgl:lwjgl-shaderc::natives-windows")
    lwjglInclude("org.lwjgl:lwjgl-shaderc::natives-linux")
    lwjglInclude("org.lwjgl:lwjgl-shaderc::natives-macos")
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
            programArgs("--quickPlaySingleplayer", "test")
        }
        named("server") {
            server()
            setConfigName("Fabric Server")
            ideConfigGenerated(true)
            runDir("runs/server")
        }
    }
}
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.loom)
    id("net.typho.big_shot_lib.plugin")
}

val version: String by project

base {
    archivesName = project.property("mod.id") as String
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

//tasks.named("revertBigShotTransforms") {
//    enabled = false
//}

bigShotLib {
    version("1.21")
    loader("fabric")

    transformInfo {
        applyPostCompileTransforms.set(false)

        shortIdentifierMethods()
        defaultDeprecatedMethods()

        injectInterface(
            "net/typho/big_shot_lib/api/client/rendering/util/NeoRenderType",
            "net/minecraft/client/renderer/RenderType"
        )
        injectInterface(
            "net/typho/big_shot_lib/api/client/rendering/util/NeoVertexConsumer",
            "com/mojang/blaze3d/vertex/VertexConsumer"
        )

        overloadArguments(
            "net/minecraft/client/renderer/RenderType",
            "net/typho/big_shot_lib/api/client/rendering/util/NeoRenderType",
            "net/typho/big_shot_lib/impl/client/Overloads",
            "convertRenderType",
            false
        )
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    maven {
        name = "Spongepowered"
        url = uri("https://repo.spongepowered.org/repository/maven-public")
    }
    ivy {
        url = uri("https://github.com/TheTypholorian/")
        patternLayout {
            artifact("[organisation]/releases/download/[revision]/[artifact]-[revision](-[classifier]).[ext]")
        }
        metadataSources {
            artifact()
        }
    }
}

dependencies {
    minecraft(libs.minecraftForAPI)
    mappings(loom.officialMojangMappings())
    implementation(kotlin("reflect"))

    compileOnly(libs.mixin)
    compileOnly(libs.mixinExtras.common)
    annotationProcessor(libs.mixinExtras.common)

    implementation(libs.asm.tree)
    implementation(libs.asm.commons)
    implementation(libs.asm.util)
}

tasks.jar {
    destinationDirectory.set(rootProject.file("build/libs/${project.version}"))
    archiveClassifier = ""
}

java {
    withSourcesJar()
}

tasks.remapSourcesJar {
    destinationDirectory.set(rootProject.file("build/libs/${project.version}"))
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonKotlin") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonKotlin", sourceSets.main.get().kotlin.sourceDirectories.filter { !it.name.endsWith("java") }.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}
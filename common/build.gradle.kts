plugins {
    id("multiloader-common")
    alias(libs.plugins.moddev)
}

val modId: String by project

base {
    archivesName = "$modId-common"
}

neoForge {
    neoFormVersion = libs.versions.neoForm
    // Automatically enable AccessTransformers if the file exists
    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = libs.versions.parchmentMC
        mappingsVersion = libs.versions.parchment
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
    compileOnly(libs.mixin)
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    compileOnly(libs.mixinExtras.common)
    annotationProcessor(libs.mixinExtras.common)

    api("org.lwjgl:lwjgl-shaderc:3.3.3")
    api("org.lwjgl:lwjgl-spvc:3.3.3")
    implementation(libs.sodium)
    implementation("com.github.TheTypholorian:big_shot_lib:api-SNAPSHOT")
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
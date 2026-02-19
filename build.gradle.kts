plugins {
    id("multiloader-common")
    alias(libs.plugins.loom)
}

val version: String by project
val modId: String by project

base {
    archivesName = modId
}

tasks.remapJar {
    archiveClassifier.set("intermediary")
}

val namedJar = tasks.register<Jar>("namedJar") {
    group = "build"
    dependsOn(tasks.classes)
    from(sourceSets.main.get().output)
}

tasks.build {
    dependsOn(namedJar)
}

repositories {
    mavenCentral()
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
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
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    api("org.lwjgl:lwjgl-shaderc:3.3.3")
    api("org.lwjgl:lwjgl-spvc:3.3.3")
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
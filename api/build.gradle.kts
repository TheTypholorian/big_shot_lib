import io.github.klahap.dotenv.DotEnvBuilder

plugins {
    kotlin("jvm")

    id("me.modmuss50.mod-publish-plugin") version "2.0.0-beta.1"
    id("io.github.klahap.dotenv") version "1.1.3"

    id("dev.isxander.modstitch.base") version "0.8.5"

    id("net.typho.big_shot_lib.plugin") version "1.0.0"

    `maven-publish`
}

bigShotLib {
    version("26.2")
    loader("fabric")
    registerJarTask(modstitch.finalJarTask)

    transformInfo {
        setupDefaults()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.typho"
            artifactId = "big_shot_lib"
            version = project.version.toString()

            artifact(tasks.jar)
            artifact(tasks.kotlinSourcesJar)
        }
    }
}

modstitch {
    modLoaderVersion = bigShotLib.deps.getLoaderVersion()
    minecraftVersion = bigShotLib.mcVersion.primaryVersion

    javaVersion.set(25)

    parchment {
        bigShotLib.deps.getParchmentVersion()?.let {
            minecraftVersion = it.first
            mappingsVersion = it.second
        }
    }

    metadata {
        modId = rootProject.property("id") as String
        modName = rootProject.property("displayName") as String
        modVersion = rootProject.property("version") as String
        modGroup = rootProject.property("group") as String

        rootProject.findProperty("authors")?.let { modAuthor = it as String }
        rootProject.findProperty("description")?.let { modDescription = it as String }
        rootProject.findProperty("license")?.let { modLicense = it as String }
        rootProject.findProperty("credits")?.let { modCredits = it as String }
    }

    finalJarTask.configure {
        archiveVersion.set("${rootProject.version}-api")
    }

    namedJarTask.configure {
        archiveVersion.set("${rootProject.version}-api")
    }
}

val env = DotEnvBuilder.dotEnv {
    addFileIfExists("$rootDir/.env")
    addFileIfExists("$projectDir/.env")
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

version = "${rootProject.property("version")}-api"
base.archivesName = rootProject.property("id") as String

repositories {
    mavenLocal()
    maven("https://api.modrinth.com/maven")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.ryanhcode.dev/releases")
    maven("https://maven.fabricmc.net")
    maven("https://maven.parchmentmc.org")
}

dependencies {
    bigShotLib.deps.modrinth("sodium")?.let { modstitchModImplementation(modDependency(it)!!) }
}

evaluationDependsOn(":common")

sourceSets {
    val commonSet = project(":common").sourceSets["main"]

    main {
        java.srcDirs(commonSet.java.srcDirs)
        kotlin.srcDirs(commonSet.kotlin.srcDirs)
        resources.srcDirs(commonSet.resources.srcDirs)
    }
}
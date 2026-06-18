import io.github.klahap.dotenv.DotEnvBuilder

plugins {
    kotlin("jvm")

    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"

    id("me.modmuss50.mod-publish-plugin") version "2.0.0-beta.1"
    id("io.github.klahap.dotenv") version "1.1.3"

    id("com.google.devtools.ksp") version "2.3.9"

    id("dev.isxander.modstitch.base") version "0.8.5"

    id("net.typho.big_shot_lib.plugin") version "1.0.0"
}

bigShotLib {
    version("1.21.1")
    loader("fabric")

    transformInfo {
        setupDefaults()
    }
}

val accessWidener = rootProject.file("src/main/resources/big_shot_lib.accesswidener")

modstitch {
    modLoaderVersion = property("deps.loader_version") as String
    minecraftVersion = "1.21.1"

    parchment {
        findProperty("deps.parchment")?.let {
            val (mc, mappings) = (it as String).split(':')
            minecraftVersion = mc
            mappingsVersion = mappings
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

    loom {
        configureLoom {
            accessWidenerPath = accessWidener
        }
    }
}

val env = DotEnvBuilder.dotEnv {
    addFileIfExists("$rootDir/.env")
    addFileIfExists("$projectDir/.env")
}

tasks.compileJava.configure {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
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
}
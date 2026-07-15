import io.github.klahap.dotenv.DotEnvBuilder
import net.typho.big_shot_lib.plugin.ModLoader
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")

    id("me.modmuss50.mod-publish-plugin") version "2.0.0-beta.1"
    id("io.github.klahap.dotenv") version "1.1.3"

    id("dev.isxander.modstitch.base") version "0.8.5"

    id("net.typho.big_shot_lib.plugin") version "1.0.0"
}

bigShotLib {
    version(sc.current.version)
    loader(when (true) {
        sc.constants["neoforge"] -> ModLoader.NEOFORGE
        sc.constants["forge"] -> ModLoader.FORGE
        sc.constants["fabric"] -> ModLoader.FABRIC
        else -> throw UnsupportedOperationException()
    })

    transformInfo {
        setupDefaults()

        clientOnlyPackages.add("net/typho/big_shot_lib/api/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/impl/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/mixin/api/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/mixin/impl/client")
    }
}

sourceSets {
    main {
        java {
            if (sc.current.parsed < "1.21.5") {
                exclude("net/typho/big_shot_lib/mixin/impl/iface/GlBufferMixin.java")
                exclude("net/typho/big_shot_lib/mixin/impl/iface/GlTextureMixin.java")
            }

            if (sc.current.parsed >= "1.21") {
                exclude("net/typho/big_shot_lib/mixin/impl/VertexFormatAccessor.java")
                exclude("net/typho/big_shot_lib/mixin/impl/RenderTypeAccessor.java")
            }

            if (sc.current.parsed < "1.21.9") {
                exclude("net/typho/big_shot_lib/mixin/impl/DebugScreenEntriesAccessor.java")
            }

            if (sc.current.parsed >= "1.21.9") {
                exclude("net/typho/big_shot_lib/mixin/impl/DebugScreenOverlayMixin.java")
            }
        }
    }
}

modstitch {
    modLoaderVersion = bigShotLib.getLoaderVersion()
    minecraftVersion = bigShotLib.mcVersion.primaryVersion

    parchment {
        bigShotLib.getParchmentVersion()?.let {
            minecraftVersion = it.first
            mappingsVersion = it.second
        }
    }

    metadata {
        modId = project.property("id") as String
        modName = project.property("displayName") as String
        modVersion = project.property("version") as String
        modGroup = project.property("group") as String

        findProperty("authors")?.let { modAuthor = it as String }
        findProperty("description")?.let { modDescription = it as String }
        findProperty("license")?.let { modLicense = it as String }
        findProperty("credits")?.let { modCredits = it as String }

        replacementProperties.put("id", project.property("id") as String)
        replacementProperties.put("version", project.property("version") as String)
        replacementProperties.put("name", project.property("displayName") as String)
        replacementProperties.put("description", project.property("description") as String)
        replacementProperties.put("authors", project.property("authors") as String)
        replacementProperties.put("license", project.property("license") as String)
        replacementProperties.put("group", project.group as String)
        replacementProperties.put("java_version", "21")
    }

    mixin {
        configs.register(project.property("id") as String)
        addMixinsToModManifest = true
    }

    /*
    finalJarTask.configure {
        archiveVersion.set("${rootProject.version}+${bigShotLib.mcVersion.primaryVersion}-${bigShotLib.loader.get().name.lowercase()}")
    }

    namedJarTask.configure {
        archiveVersion.set("${rootProject.version}+${bigShotLib.mcVersion.primaryVersion}-${bigShotLib.loader.get().name.lowercase()}")
    }
     */

    classTweaker.set(sc.process(
        rootProject.file("src/main/resources/classTweaker.ct"),
        "build/classTweaker.ct"
    ))

    moddevgradle {
        when (bigShotLib.loader.get()) {
            ModLoader.FORGE -> {
                forgeVersion = bigShotLib.getForgeLoaderVersion()
            }
            ModLoader.NEOFORGE -> {
                neoForgeVersion = bigShotLib.getNeoForgeLoaderVersion()
            }
            else -> {}
        }

        defaultRuns()
    }
}

val env = DotEnvBuilder.dotEnv {
    addFileIfExists("$rootDir/.env")
    addFileIfExists("$projectDir/.env")
}

tasks.compileJava.configure {
    val javaCompat = when {
        sc.current.parsed >= "26.1" -> "25"
        sc.current.parsed >= "1.20.5" -> "21"
        sc.current.parsed >= "1.18" -> "17"
        sc.current.parsed >= "1.17" -> "16"
        else -> "8"
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

java {
    val javaCompat = when {
        sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
        sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
        sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
        sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
        else -> JavaVersion.VERSION_1_8
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

kotlin {
    jvmToolchain(
        when {
            sc.current.parsed >= "26.1" -> 25
            sc.current.parsed >= "1.20.5" -> 21
            sc.current.parsed >= "1.18" -> 17
            sc.current.parsed >= "1.17" -> 16
            else -> 8
        }
    )
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

tasks.processResources {
    when (bigShotLib.loader.get()) {
        ModLoader.NEOFORGE -> {
            exclude("**/forge.mods.toml")
            exclude("fabric.mod.json")
        }
        ModLoader.FORGE -> {
            exclude("**/neoforge.mods.toml")
            exclude("fabric.mod.json")
        }
        ModLoader.FABRIC -> {
            exclude("**/neoforge.mods.toml")
            exclude("**/forge.mods.toml")
        }
        else -> {}
    }
}

version = "${property("version")}+${bigShotLib.mcVersion.primaryVersion}-${bigShotLib.loader.get().name.lowercase()}"
base.archivesName = property("id") as String

repositories {
    mavenLocal()
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.ryanhcode.dev/releases")
    maven("https://maven.fabricmc.net")
    maven("https://maven.parchmentmc.org")
}

dependencies {
    when (bigShotLib.loader.get()) {
        ModLoader.NEOFORGE, ModLoader.FORGE -> {
            modstitchModImplementation(modDependency(bigShotLib.modrinthDep("kotlin-for-forge") ?: error("No KFF version"))!!)
        }
        ModLoader.FABRIC -> {
            modstitchModImplementation(modDependency(bigShotLib.modrinthDep("fabric-api") ?: error("No fapi version"))!!)
            modstitchModImplementation(modDependency(bigShotLib.modrinthDep("fabric-language-kotlin") ?: error("No FLK version"))!!)
        }
        else -> {}
    }

    bigShotLib.modrinthDep("sodium")?.let { modstitchModImplementation(modDependency(it)!!) }
}

sourceSets.named("main") {
    java.srcDirs(project(":api").sourceSets["main"].java.srcDirs)
    kotlin.srcDirs(project(":api").sourceSets["main"].kotlin.srcDirs)
    resources.srcDirs(project(":api").sourceSets["main"].resources.srcDirs)
}
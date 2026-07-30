import io.github.klahap.dotenv.DotEnvBuilder
import net.typho.big_shot_lib.plugin.ModLoader

plugins {
    kotlin("jvm")

    id("me.modmuss50.mod-publish-plugin") version "2.0.0-beta.1"
    id("io.github.klahap.dotenv") version "1.1.3"

    id("dev.isxander.modstitch.base") version "0.8.5"

    id("net.typho.big_shot_lib.plugin") version "1.0.0"
}

bigShotLib {
    version(sc.current.version)
    loader(ModLoader[sc.current.project.substringAfterLast('_')])
    // TODO
    //registerJarTask(modstitch.finalJarTask)

    transformInfo {
        setupDefaults()

        clientOnlyPackages.add("net/typho/big_shot_lib/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/mixin/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/mixin/fabric/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/mixin/forge/client")
        clientOnlyPackages.add("net/typho/big_shot_lib/mixin/neoforge/client")
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
        replacementProperties.put("displayName", project.property("displayName") as String)
        replacementProperties.put("description", project.property("description") as String)
        replacementProperties.put("authors", project.property("authors") as String)
        replacementProperties.put("license", project.property("license") as String)
        replacementProperties.put("group", project.group as String)
        replacementProperties.put("loader", bigShotLib.loader.get().name.lowercase())
        replacementProperties.put("java_version", javaVersion.toString())
    }

    mixin {
        val modId = metadata.modId.get()
        configs.register(modId)
        configs.register("$modId.${bigShotLib.loader.get().name.lowercase()}")
        addMixinsToModManifest = true
    }

    finalJarTask.configure {
        archiveVersion.set("${rootProject.version}+${bigShotLib.mcVersion.primaryVersion}-${bigShotLib.loader.get().name.lowercase()}")
    }

    namedJarTask.configure {
        archiveVersion.set("${rootProject.version}+${bigShotLib.mcVersion.primaryVersion}-${bigShotLib.loader.get().name.lowercase()}")
    }

    classTweaker.set(sc.process(
        rootProject.file("src/main/resources/classTweaker.ct"),
        "build/classTweaker.ct"
    ))

    moddevgradle {
        when (bigShotLib.loader.get()) {
            ModLoader.FORGE -> {
                forgeVersion = bigShotLib.deps.getForgeLoaderVersion()
            }
            ModLoader.NEOFORGE -> {
                neoForgeVersion = bigShotLib.deps.getNeoForgeLoaderVersion()
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

kotlin {
    jvmToolchain(25)
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
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
            modstitchModImplementation(modDependency(bigShotLib.deps.modrinth("kotlin-for-forge") ?: error("No KFF version"))!!)
        }
        ModLoader.FABRIC -> {
            modstitchModImplementation(modDependency(bigShotLib.deps.modrinth("fabric-api") ?: error("No fapi version"))!!)
            modstitchModImplementation(modDependency(bigShotLib.deps.modrinth("fabric-language-kotlin") ?: error("No FLK version"))!!)
        }
        else -> {}
    }

    bigShotLib.deps.modrinth("sodium")?.let { modstitchModImplementation(modDependency(it)!!) }
    modstitchRuntimeOnly(kotlin("reflect"))
}

tasks.processResources {
    dependsOn(project(":agent").tasks.build)
}

evaluationDependsOn(":api")

sourceSets {
    val apiSet = project(":api").sourceSets["main"]

    main {
        java.srcDirs(apiSet.java.srcDirs)
        kotlin.srcDirs(apiSet.kotlin.srcDirs)
        resources.srcDirs(apiSet.resources.srcDirs)

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
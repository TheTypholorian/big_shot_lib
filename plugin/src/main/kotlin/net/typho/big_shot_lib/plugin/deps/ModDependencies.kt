package net.typho.big_shot_lib.plugin.deps

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import net.typho.big_shot_lib.plugin.MCVersion
import net.typho.big_shot_lib.plugin.ManifestCache
import net.typho.big_shot_lib.plugin.ModLoader
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.io.File
import java.time.Instant
import java.util.Properties
import java.util.function.Function
import javax.inject.Inject

abstract class ModDependencies(
    @JvmField
    val file: File
) {
    @Inject
    constructor(project: Project) : this(project.file("dependency_versions.properties"))

    abstract val mcVersionProperty: Property<String>
    abstract val loader: Property<ModLoader>

    fun cacheDependencyVersion(dependency: String, versionIdToName: Function<String, String>, latest: String?, update: Boolean = false): String? {
        var versionId = latest
        val properties = Properties()

        if (file.exists()) {
            file.inputStream().use(properties::load)
        }

        var cachedVersion = properties.getProperty(dependency)

        if (cachedVersion == "null") {
            cachedVersion = null
        }

        if (versionId != cachedVersion) {
            if (cachedVersion == null || update) {
                if (update) {
                    println("[Big Shot Lib] Updating dependency $dependency from ${versionIdToName.apply(cachedVersion)} to ${versionId?.let(versionIdToName::apply) ?: "Unknown"}.")
                }

                properties.setProperty(dependency, versionId)
                file.outputStream().use { properties.store(it, "[Big Shot Lib] Cached dependency versions. Run the updateDependencyVersions task to update them all to latest.") }
            } else {
                println("[Big Shot Lib] Update available for dependency $dependency (${versionIdToName.apply(cachedVersion)} to ${versionId?.let(versionIdToName::apply) ?: "Unknown"}). Run the updateDependencyVersions task to update.")

                versionId = cachedVersion
            }
        }

        return versionId
    }

    @JvmOverloads
    fun getFabricLoaderVersion(update: Boolean = false): String {
        val body = ManifestCache.fabricLoaderVersions

        try {
            val json = JsonParser.parseString(body).asJsonArray[0].asJsonObject
            var versionId = json.get("version").asString

            versionId = cacheDependencyVersion("fabric-loader", { it }, versionId, update)
            println("[Big Shot Lib] Using $versionId for fabric loader")
            return versionId
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }
    }

    fun getMcVersionFromNeoForge(version: String): String {
        val tokens = version.split('.')

        if (tokens[0].toInt() >= 26) {
            var mcVersion = "${tokens[0]}.${tokens[1]}"

            if (tokens[2] != "0") {
                mcVersion += ".${tokens[2]}"
            }

            val snapshot = version.split('+')

            if (snapshot.size == 2) {
                mcVersion += "-${snapshot[1]}"
            }

            return mcVersion
        }

        return "1.${tokens[0]}.${tokens[1]}"
    }

    fun getMcVersionFromForge(version: String): String {
        return version.substringBefore('-')
    }

    @JvmOverloads
    fun getNeoForgeLoaderVersion(update: Boolean = false): String? {
        val mc = MCVersion[mcVersionProperty.get()]
        val body = ManifestCache.neoForgeLoaderVersions

        try {
            val versions = JsonParser.parseString(body).asJsonObject.getAsJsonArray("versions").reversed()
            for (version in versions) {
                var versionId = version.asString

                if (getMcVersionFromNeoForge(versionId) == mc.primaryVersion) {
                    versionId = cacheDependencyVersion("neoforge-loader", { it }, versionId, update)
                    println("[Big Shot Lib] Using $versionId for neoforge loader")
                    return versionId
                }
            }
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }

        return null
    }

    @JvmOverloads
    fun getForgeLoaderVersion(update: Boolean = false): String? {
        val mc = MCVersion[mcVersionProperty.get()]
        val body = ManifestCache.forgeLoaderVersions

        try {
            val versions = JsonParser.parseString(body).asJsonObject.getAsJsonArray("versions").reversed()

            for (version in versions) {
                var versionId = version.asString

                if (getMcVersionFromForge(versionId) == mc.primaryVersion) {
                    versionId = cacheDependencyVersion("forge-loader", { it }, versionId, update)
                    println("[Big Shot Lib] Using $versionId for forge loader")
                    return versionId
                }
            }
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }

        return null
    }

    @JvmOverloads
    fun getLoaderVersion(update: Boolean = false): String? {
        return when (val loader = loader.get()) {
            ModLoader.FABRIC -> getFabricLoaderVersion(update)
            ModLoader.NEOFORGE -> getNeoForgeLoaderVersion(update)
            ModLoader.FORGE -> getForgeLoaderVersion(update)
            else -> throw IllegalArgumentException("No loader version for $loader")
        }
    }

    fun getParchmentVersion(): Pair<String, String>? {
        val mc = MCVersion[mcVersionProperty.get()]

        return mc.parchmentVersion
    }

    @JvmOverloads
    fun getModrinthProjectVersion(projectId: String, update: Boolean = false): String? {
        val mc = MCVersion[mcVersionProperty.get()]
        val loader = loader.get()
        val body = ManifestCache.getModrinthVersions(projectId)

        try {
            val json = JsonParser.parseString(body).asJsonArray

            val versionIdToName = mutableMapOf<String, String?>()

            var versionId: String? = null
            var mostRecentTime: Instant? = null

            for (versionElement in json) {
                val version = versionElement.asJsonObject
                val time = Instant.parse(version.get("date_published").asString)
                val id = version.get("id").asString

                if (version.getAsJsonArray("game_versions").none { a -> mc.versions.any { b -> a.asString.equals(b, ignoreCase = true) } }) {
                    continue
                }

                if (version.getAsJsonArray("loaders").none { loader.name.equals(it.asString, ignoreCase = true) }) {
                    continue
                }

                versionIdToName[id] = version.get("name")?.asString

                if (mostRecentTime == null || time.isAfter(mostRecentTime)) {
                    versionId = id
                    mostRecentTime = time
                }
            }

            versionId = cacheDependencyVersion(projectId, { versionIdToName[it] ?: it }, versionId, update)
            println("[Big Shot Lib] Using ${versionIdToName.getOrDefault(versionId, versionId)} for modrinth project $projectId on ${mc.primaryVersion} $loader")
            return versionId
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }
    }

    fun getVersion(dependency: String, update: Boolean = false): String? {
        return when (dependency) {
            "fabric-loader" -> getFabricLoaderVersion(update)
            "neoforge-loader" -> getNeoForgeLoaderVersion(update)
            "forge-loader" -> getForgeLoaderVersion(update)
            else -> getModrinthProjectVersion(dependency, update)
        }
    }

    fun modrinth(projectId: String, update: Boolean = false): String? {
        return getModrinthProjectVersion(projectId, update)?.let { "maven.modrinth:$projectId:$it" }
    }
}

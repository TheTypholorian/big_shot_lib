package net.typho.big_shot_lib.plugin.dependencies

import com.google.gson.Gson
import com.google.gson.JsonParser
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import net.typho.big_shot_lib.plugin.MCVersion
import net.typho.big_shot_lib.plugin.ModLoader
import org.gradle.api.Project
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Properties
import java.util.function.Function
import javax.xml.parsers.DocumentBuilderFactory

val Project.dependencyPropertyFile
    get() = project.file("dependency_versions.properties")

fun Project.cacheDependencyVersion(dependency: String, versionIdToName: Function<String, String>, latest: String, update: Boolean = false): String {
    var versionId = latest
    val properties = Properties()
    val propertiesFile = project.dependencyPropertyFile

    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use(properties::load)
    }

    val cachedVersion = properties.getProperty(dependency)

    if (versionId != cachedVersion) {
        if (cachedVersion == null || update) {
            if (update) {
                println("[Big Shot Lib] Updating dependency $dependency from ${versionIdToName.apply(cachedVersion)} to ${versionIdToName.apply(versionId)}.")
            }

            properties.setProperty(dependency, versionId)
            propertiesFile.outputStream().use { properties.store(it, "[Big Shot Lib] Cached dependency versions. Run the updateDependencyVersions task to update them all to latest.") }
        } else {
            println("[Big Shot Lib] Update available for dependency $dependency (${versionIdToName.apply(cachedVersion)} to ${versionIdToName.apply(versionId)}). Run the updateDependencyVersions task to update.")

            versionId = cachedVersion
        }
    }

    return versionId
}

@JvmOverloads
fun Project.getFabricLoaderVersion(update: Boolean = false): String {
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://meta.fabricmc.net/v2/versions/loader?limit=1"))
        .header("Accept", "application/json")
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() == 404) {
        throw RuntimeException("[Big Shot Lib] Unable to find fabric loader version")
    }

    val body = response.body()
    val json = JsonParser.parseString(body).asJsonArray[0].asJsonObject
    var versionId = json.get("version").asString

    versionId = cacheDependencyVersion("fabric-loader", { it }, versionId, update)
    println("[Big Shot Lib] Using $versionId for fabric loader")
    return versionId
}

@JvmOverloads
fun Project.getNeoForgeLoaderVersion(update: Boolean = false): String {
    val ext = project.extensions.getByType(BigShotLibPluginExtension::class.java)
    val mc = MCVersion[ext.version.get()]

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/neoforge"))
        .header("Accept", "application/json")
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() == 404) {
        throw RuntimeException("[Big Shot Lib] Unable to find neoforge loader version")
    }

    val body = response.body()
    val versions = JsonParser.parseString(body).asJsonObject.getAsJsonArray("versions").reversed()

    for (version in versions) {
        var versionId = version.asString

        if (versionId.startsWith(mc.forgeVersionPrefix)) {
            versionId = cacheDependencyVersion("neoforge-loader", { it }, versionId, update)
            println("[Big Shot Lib] Using $versionId for neoforge loader")
            return versionId
        }
    }

    throw RuntimeException("[Big Shot Lib] No version of neoforge loader matches ${mc.primaryVersion} (looking for prefix ${mc.forgeVersionPrefix})")
}

@JvmOverloads
fun Project.getForgeLoaderVersion(update: Boolean = false): String {
    val ext = project.extensions.getByType(BigShotLibPluginExtension::class.java)
    val mc = MCVersion[ext.version.get()]

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://maven.minecraftforge.net/net/minecraftforge/forge/maven-metadata.xml"))
        .header("Accept", "text/xml")
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() == 404) {
        throw RuntimeException("[Big Shot Lib] Unable to find forge loader version")
    }

    val body = response.body()
    val versions = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(body).getElementsByTagName("version")

    repeat(versions.length) { i ->
        var versionId = versions.item(i).textContent

        if (versionId.startsWith(mc.forgeVersionPrefix)) {
            versionId = cacheDependencyVersion("forge-loader", { it }, versionId, update)
            println("[Big Shot Lib] Using $versionId for forge loader")
            return versionId
        }
    }

    throw RuntimeException("[Big Shot Lib] No version of forge loader matches ${mc.primaryVersion} (looking for prefix ${mc.forgeVersionPrefix})")
}

@JvmOverloads
fun Project.getLoaderVersion(update: Boolean = false): String {
    val ext = project.extensions.getByType(BigShotLibPluginExtension::class.java)

    return when (val loader = ext.loader.get()) {
        ModLoader.FABRIC -> getFabricLoaderVersion(update)
        ModLoader.NEOFORGE -> getNeoForgeLoaderVersion(update)
        ModLoader.FORGE -> getForgeLoaderVersion(update)
        else -> throw IllegalArgumentException("No loader version for $loader")
    }
}

fun Project.getParchmentVersion(): Pair<MCVersion, String>? {
    val ext = project.extensions.getByType(BigShotLibPluginExtension::class.java)
    val mc = MCVersion[ext.version.get()]

    return mc.parchmentVersion
}

@JvmOverloads
fun Project.getModrinthProjectVersion(projectId: String, update: Boolean = false): String {
    val ext = project.extensions.getByType(BigShotLibPluginExtension::class.java)
    val mc = MCVersion[ext.version.get()]
    val loader = ext.loader.get()

    val gson = Gson()

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(buildString {
            append("https://api.modrinth.com/v2/project/$projectId/version?")

            if (loader != ModLoader.NONE) {
                append("loaders=${URLEncoder.encode(gson.toJson(listOf(loader.name.lowercase())), StandardCharsets.UTF_8)}&")
            }

            append("game_versions=${URLEncoder.encode(gson.toJson(mc.versions), StandardCharsets.UTF_8)}")
        }))
        .header("Accept", "application/json")
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() == 404) {
        throw RuntimeException("[Big Shot Lib] Unable to find modrinth project $projectId")
    }

    val body = response.body()
    val json = JsonParser.parseString(body).asJsonArray

    val versionIdToName = mutableMapOf<String, String?>()

    var versionId: String? = null
    var mostRecentTime: Instant? = null

    for (versionElement in json) {
        val version = versionElement.asJsonObject
        val time = Instant.parse(version.get("date_published").asString)

        versionIdToName[version.get("id").asString] = version.get("name")?.asString

        if (mostRecentTime == null || time.isAfter(mostRecentTime)) {
            versionId = version.get("id").asString
            mostRecentTime = time
        }
    }

    versionId ?: throw RuntimeException("[Big Shot Lib] No version of modrinth project $projectId matches ${mc.primaryVersion} $loader")
    versionId = cacheDependencyVersion(projectId, { versionIdToName[it] ?: it }, versionId, update)
    println("[Big Shot Lib] Using ${versionIdToName.getOrDefault(versionId, versionId)} for modrinth project $projectId on ${mc.primaryVersion} $loader")
    return versionId
}
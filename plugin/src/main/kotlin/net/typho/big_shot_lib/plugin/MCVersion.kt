package net.typho.big_shot_lib.plugin

import com.google.gson.JsonParser
import java.io.Serializable
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import kotlin.sequences.sortedWith

data class MCVersion(
    @JvmField
    val versions: List<String>
) : Serializable {
    /**
     * The Minecraft version range for fabric/quilt
     */
    @Transient
    val fabricVersionRange: String = if (versions.size == 1) versions.first() else ">=${versions.first()} <=${versions.last()}"
    /**
     * The Minecraft version range for neoforge/forge
     */
    @Transient
    val forgeVersionRange: String = if (versions.size == 1) "[${versions.first()}]" else "[${versions.first()}, ${versions.last()}]"
    /**
     * The Minecraft version that is used in dev
     */
    @Transient
    val primaryVersion: String = versions.last()
    /**
     * Additional Minecraft versions that have no modder-relevant change(s) (ex. patches)
     */
    @Transient
    val additionalVersions: List<String> = versions.toMutableList().apply { removeLast() }
    /**
     * The prefix for the forge version for this Minecraft version
     */
    @Transient
    val forgeVersionPrefix: String = primaryVersion.substringAfter("1.")
    /**
     * The prefix for the neoforge version for this Minecraft version
     */
    @Transient
    val neoForgeVersionPrefix: String = if (forgeVersionPrefix.indexOf('.') == -1) "$forgeVersionPrefix.0" else forgeVersionPrefix
    /**
     * The version of Parchment to use for this Minecraft version, in the format `("1.21.1", "2024.11.17")`.
     * Might be null if Parchment doesn't support this version.
     * For Minecraft versions without an explicit parchment version (ex. 1.19, 1.19.1, 1.20, 1.21.2), it uses the next parchment version.
     * This results in all versions before 1.16.5 using 1.16.5 parchment, which is probably fine.
     *
     * **Note**: This value is not cached, so you can register extra parchment versions by putting `MCVersion.registerParchment(mc, parchment)` at the start of your build script.
     */
    val parchmentVersion: Pair<MCVersion, String>?
        get() = PARCHMENT_VERSIONS.sortedWith { a, b -> b.first.compareTo(a.first) }.firstOrNull { (mc, parchment) -> this >= mc }

    operator fun compareTo(other: MCVersion) = VERSIONS.indexOf(this).compareTo(VERSIONS.indexOf(other))

    operator fun compareTo(other: String) = compareTo(MCVersion[other])

    fun getVersionRange(loader: ModLoader) = when (loader) {
        ModLoader.FABRIC -> fabricVersionRange
        ModLoader.NEOFORGE, ModLoader.FORGE -> forgeVersionRange
        else -> null
    }

    override fun toString(): String {
        return "MCVersion(primaryVersion='$primaryVersion', additionalVersions=$additionalVersions, parchmentVersion=${parchmentVersion?.let { "${it.first.primaryVersion}:${it.second}" }}, fabricVersionRange='$fabricVersionRange', forgeVersionRange='$forgeVersionRange', neoForgeVersionPrefix='$neoForgeVersionPrefix', forgeVersionPrefix='$forgeVersionPrefix')"
    }

    companion object {
        /**
         * Most recent versions are at the start of the list
         */
        @JvmField
        val VERSIONS = mutableListOf<MCVersion>()
        @JvmField
        val PARCHMENT_VERSIONS = mutableListOf<Pair<MCVersion, String>>()

        @JvmStatic
        fun registerParchment(mc: MCVersion, parchment: String) {
            PARCHMENT_VERSIONS.add(mc to parchment)
        }

        @JvmStatic
        fun registerParchment(mc: String, parchment: String) {
            registerParchment(MCVersion[mc], parchment)
        }

        init {
            val client = HttpClient.newHttpClient()
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"))
                .header("Accept", "application/json")
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() == 404) {
                throw RuntimeException("[Big Shot Lib] Unable to get Minecraft version manifest")
            }

            val body = response.body()
            val versions = JsonParser.parseString(body).asJsonObject.getAsJsonArray("versions")
                .asSequence()
                .map { it.asJsonObject }
                .filter { it.get("type").asString == "release" }
                .map { it.get("id").asString to Instant.parse(it.get("releaseTime").asString) }
                .sortedWith { a, b -> a.second.compareTo(b.second) }
                .map { it.first }
                .toList()

            val multiVersions = linkedMapOf<String, MutableList<String>>()
            val gameDropIndex = versions.indexOf("26.1")

            versions.forEachIndexed { index, version ->
                val groupVersion = if (index > gameDropIndex) {
                    val versionComponents = version.split('.')
                    "${versionComponents[0]}.${versionComponents[1]}"
                } else {
                    when (version) {
                        "1.19" -> "1.19.1"
                        "1.20" -> "1.20.1"
                        "1.21" -> "1.21.1"
                        else -> version
                    }
                }

                multiVersions.computeIfAbsent(groupVersion) { mutableListOf() }.add(version)
            }

            for (version in multiVersions.values) {
                VERSIONS.add(MCVersion(version))
            }

            VERSIONS.reverse()

            registerParchment("1.16.5", "2022.03.06")
            registerParchment("1.17.1", "2021.12.12")
            registerParchment("1.18.2", "2022.11.06")
            registerParchment("1.19.2", "2022.11.27")
            registerParchment("1.19.3", "2023.06.25")
            registerParchment("1.19.4", "2023.06.26")
            registerParchment("1.20.1", "2023.09.03")
            registerParchment("1.20.2", "2023.12.10")
            registerParchment("1.20.3", "2023.12.31")
            registerParchment("1.20.4", "2024.04.14")
            registerParchment("1.20.6", "2024.06.16")
            registerParchment("1.21.1", "2024.11.17")
            registerParchment("1.21.3", "2024.12.07")
            registerParchment("1.21.4", "2025.03.23")
            registerParchment("1.21.5", "2025.06.15")
            registerParchment("1.21.6", "2025.06.29")
            registerParchment("1.21.7", "2025.07.18")
            registerParchment("1.21.8", "2025.09.14")
            registerParchment("1.21.9", "2025.10.05")
            registerParchment("1.21.10", "2025.10.12")
            registerParchment("1.21.11", "2025.12.20")
        }

        @JvmStatic
        operator fun get(version: String): MCVersion {
            val version = if (version.endsWith(".0")) version.substringBefore(".0") else version
            return VERSIONS.firstOrNull { it.versions.contains(version) }
                ?: throw NullPointerException("Nonexistent Minecraft version '$version' (it should be in the format '1.21', '1.21.1', '26.1.2', etc.)")
        }
    }
}
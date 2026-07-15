package net.typho.big_shot_lib.plugin

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object ManifestCache {
    val fabricLoaderVersions: String by lazy {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://meta.fabricmc.net/v2/versions/loader?limit=1"))
            .header("Accept", "application/json")
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == 404) {
            throw RuntimeException("[Big Shot Lib] Unable to get fabric loader versions")
        }

        response.body()
    }

    val neoForgeLoaderVersions: String by lazy {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/neoforge"))
            .header("Accept", "application/json")
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == 404) {
            throw RuntimeException("[Big Shot Lib] Unable to get neoforge loader versions")
        }

        response.body()
    }

    val forgeLoaderVersions: String by lazy {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://maven.minecraftforge.net/api/maven/versions/releases/net/minecraftforge/forge"))
            .header("Accept", "application/json")
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == 404) {
            throw RuntimeException("[Big Shot Lib] Unable to get forge loader versions")
        }

        response.body()
    }

    private val modrinthVersions = mutableMapOf<String, String>()

    @JvmStatic
    fun getModrinthVersions(projectId: String) = modrinthVersions.computeIfAbsent(projectId) {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.modrinth.com/v2/project/$projectId/version"))
            .header("Accept", "application/json")
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == 404) {
            throw RuntimeException("[Big Shot Lib] Unable to get modrinth project $projectId")
        }

        response.body()
    }
}
package net.typho.big_shot_lib.common.loading

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

data class ModVersion(
    @JvmField
    val userReadable: String,
    @JvmField
    val release: List<Int>,
    @JvmField
    val snapshot: List<Int>
) {
    companion object {
        @JvmStatic
        fun generateUserReadableVersion(
            release: List<Int>,
            snapshot: List<Int>
        ): String {
            return if (snapshot.isEmpty()) {
                release.joinToString(".")
            } else {
                "${release.joinToString(".")}-snapshot.${snapshot.joinToString(".")}"
            }
        }
    }

    operator fun compareTo(other: ModVersion): Int {
        val selfRelease = release.toMutableList()
        val otherRelease = other.release.toMutableList()

        repeat(release.size - other.release.size) {
            otherRelease.add(0)
        }

        repeat(other.release.size - release.size) {
            selfRelease.add(0)
        }

        repeat(selfRelease.size) { i ->
            val a = selfRelease[i]
            val b = otherRelease[i]

            if (a != b) {
                return a.compareTo(b)
            }
        }

        if (snapshot.isEmpty() && other.snapshot.isNotEmpty()) {
            return 1
        } else if (other.snapshot.isEmpty() && snapshot.isNotEmpty()) {
            return -1
        }

        val selfSnapshot = snapshot.toMutableList()
        val otherSnapshot = other.snapshot.toMutableList()

        repeat(snapshot.size - other.snapshot.size) {
            otherSnapshot.add(0)
        }

        repeat(other.snapshot.size - snapshot.size) {
            selfSnapshot.add(0)
        }

        repeat(selfSnapshot.size) { i ->
            val a = selfSnapshot[i]
            val b = otherSnapshot[i]

            if (a != b) {
                return a.compareTo(b)
            }
        }

        return 0
    }

    class Builder @JvmOverloads constructor(
        @JvmField
        var userReadable: String? = null,
        vararg release: Int
    ) {
        @JvmField
        var release: List<Int> = release.toList()
        @JvmField
        var snapshot: List<Int> = listOf()

        fun snapshot(vararg snapshot: Int): Builder {
            this.snapshot = snapshot.toList()
            return this
        }

        fun build() = ModVersion(
            userReadable ?: generateUserReadableVersion(release, snapshot),
            release,
            snapshot
        )
    }

    object JsonCodec : JsonSerializer<ModVersion>, JsonDeserializer<ModVersion> {
        override fun serialize(
            src: ModVersion,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            val json = JsonObject()

            json.addProperty("user_readable", src.userReadable)
            json.add("release", JsonArray().also { src.release.mapTo(it.asList()) { JsonPrimitive(it) } })

            if (src.snapshot.isNotEmpty()) {
                json.add("snapshot", JsonArray().also { src.snapshot.mapTo(it.asList()) { JsonPrimitive(it) } })
            }

            return json
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ModVersion {
            if (json.isJsonObject) {
                val json = json.asJsonObject
                val userReadable = json["user_readable"]?.asString
                val release = (json.getAsJsonArray("release") ?: throw JsonParseException("Must specify a release value in mod version")).map { it.asInt }
                val snapshot = json.getAsJsonArray("snapshot")?.map { it.asInt } ?: listOf()
                return ModVersion(
                    userReadable ?: generateUserReadableVersion(release, snapshot),
                    release,
                    snapshot
                )
            } else if (json.isJsonArray) {
                val json = json.asJsonArray
                val release = json.map { it.asInt }
                return ModVersion(
                    generateUserReadableVersion(release, listOf()),
                    release,
                    listOf()
                )
            } else {
                throw JsonParseException("Mod version must be either an object or an array")
            }
        }
    }
}

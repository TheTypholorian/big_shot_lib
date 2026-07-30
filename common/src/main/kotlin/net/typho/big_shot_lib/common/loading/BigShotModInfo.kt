package net.typho.big_shot_lib.common.loading

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import net.typho.big_shot_lib.common.annotation.Environment
import java.lang.reflect.Type
import java.util.function.Consumer
import kotlin.jvm.java

data class BigShotModInfo(
    @JvmField
    val id: String,
    @JvmField
    val version: String,

    @JvmField
    val environment: Environment?,
    @JvmField
    val entrypoints: Map<String, List<ModEntrypoint>>,
    @JvmField
    val mixins: List<ModMixinConfig>,

    @JvmField
    val license: String?,
    @JvmField
    val authors: List<ModAuthor>,
    @JvmField
    val contact: Map<String, String>,
    @JvmField
    val icon: String?,

    @JvmField
    val extra: Map<String, JsonElement>
) {
    companion object {
        /**
         * TODO support .toml format
         */
        const val FILE_NAME = "big_shot.mod.json"
        @JvmStatic
        @get:JvmName("getGson")
        val GSON by lazy {
            GsonBuilder()
                .registerTypeAdapter(BigShotModInfo::class.java, JsonCodec)
                .registerTypeAdapter(ModAuthor::class.java, ModAuthor.JsonCodec)
                .registerTypeAdapter(ModMixinConfig::class.java, ModMixinConfig.JsonCodec)
                .registerTypeAdapter(ModVersion::class.java, ModVersion.JsonCodec) // TODO
                .registerTypeAdapter(Environment::class.java, Environment.JsonCodec)
                .create()
        }
    }

    fun <T> invokeEntrypoints(
        type: String,
        entrypointType: Class<T>,
        args: Map<Class<*>, Any?>,
        out: Consumer<T>
    ) {
        entrypoints[type]?.forEach { entrypoint ->
            out.accept(entrypoint.getOrConstruct(
                entrypointType,
                args
            ))
        }
    }

    object JsonCodec : JsonSerializer<BigShotModInfo>, JsonDeserializer<BigShotModInfo> {
        override fun serialize(
            src: BigShotModInfo,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonObject().apply {
                addProperty("id", src.id)
                addProperty("version", src.version)

                add("environment", context.serialize(src.environment))
                add("entrypoints", context.serialize(src.entrypoints))
                add("mixins", context.serialize(src.mixins))

                addProperty("mixins", src.license)
                add("authors", context.serialize(src.authors))
                add("contact", context.serialize(src.contact))
                addProperty("icon", src.icon)

                add("extra", context.serialize(src.extra))
            }
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): BigShotModInfo {
            val json = json.asJsonObject
            return BigShotModInfo(
                json.getAsJsonPrimitive("id").asString,
                json.getAsJsonPrimitive("version").asString,

                context.deserialize(json.get("environment"), Environment::class.java),
                context.deserialize(json.get("entrypoints"), TypeToken.getParameterized(Map::class.java, String::class.java, TypeToken.getParameterized(List::class.java, ModEntrypoint::class.java).type).type) ?: mapOf(),
                context.deserialize(json.get("mixins"), TypeToken.getParameterized(List::class.java, ModMixinConfig::class.java).type) ?: listOf(),

                json.getAsJsonPrimitive("license")?.asString,
                context.deserialize(json.get("authors"), TypeToken.getParameterized(List::class.java, ModAuthor::class.java).type) ?: listOf(),
                context.deserialize(json.get("contact"), TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type) ?: mapOf(),
                json.getAsJsonPrimitive("icon")?.asString,

                json.getAsJsonObject("extra")?.asMap() ?: mapOf(),
            )
        }
    }
}

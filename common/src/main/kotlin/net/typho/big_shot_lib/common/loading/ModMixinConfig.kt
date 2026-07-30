package net.typho.big_shot_lib.common.loading

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import net.typho.big_shot_lib.common.annotation.Environment
import java.lang.reflect.Type

data class ModMixinConfig(
    @JvmField
    val path: String,
    @JvmField
    val environment: Environment?
) {
    object JsonCodec : JsonSerializer<ModMixinConfig>, JsonDeserializer<ModMixinConfig> {
        override fun serialize(
            src: ModMixinConfig,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return if (src.environment == null) {
                JsonPrimitive(src.path)
            } else {
                val json = JsonObject()

                json.addProperty("path", src.path)
                json.add("environment", context.serialize(src.environment))

                json
            }
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ModMixinConfig {
            return if (json.isJsonPrimitive) {
                ModMixinConfig(json.asString, null)
            } else if (json.isJsonObject) {
                val json = json.asJsonObject
                ModMixinConfig(
                    json.getAsJsonPrimitive("name").asString,
                    json.get("environment")?.let { context.deserialize(it, Environment::class.java) }
                )
            } else {
                throw JsonParseException("Mod mixin config must be either a string or an object")
            }
        }
    }
}

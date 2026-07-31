package net.typho.big_shot_lib.common.loading

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class ModDependencies(
    @JvmField
    val required: Map<String, String>,
    @JvmField
    val incompatible: Map<String, String>
) {
    object JsonCodec : JsonSerializer<ModDependencies>, JsonDeserializer<ModDependencies> {
        override fun serialize(
            src: ModDependencies,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonObject().apply {
                add("required", context.serialize(src.required))
                add("incompatible", context.serialize(src.incompatible))
            }
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ModDependencies {
            val json = json.asJsonObject
            return ModDependencies(
                context.deserialize(json.get("required"), TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type),
                context.deserialize(json.get("incompatible"), TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type)
            )
        }
    }
}
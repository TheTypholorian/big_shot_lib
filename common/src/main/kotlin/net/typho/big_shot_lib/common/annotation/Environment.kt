package net.typho.big_shot_lib.common.annotation

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

enum class Environment {
    CLIENT,
    SERVER;

    object JsonCodec : JsonSerializer<Environment>, JsonDeserializer<Environment> {
        override fun serialize(
            src: Environment,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonPrimitive(src.name)
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): Environment {
            return valueOf(json.asString.uppercase())
        }
    }
}
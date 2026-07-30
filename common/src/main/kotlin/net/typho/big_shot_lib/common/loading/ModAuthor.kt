package net.typho.big_shot_lib.common.loading

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

data class ModAuthor(
    @JvmField
    val name: String,
    @JvmField
    val contact: Map<String, String>
) {
    object JsonCodec : JsonSerializer<ModAuthor>, JsonDeserializer<ModAuthor> {
        override fun serialize(
            src: ModAuthor,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return if (src.contact.isEmpty()) {
                JsonPrimitive(src.name)
            } else {
                val contact = JsonObject()
                src.contact.mapValuesTo(contact.asMap()) { JsonPrimitive(it.value) }

                val json = JsonObject()

                json.addProperty("name", src.name)
                json.add("contact", contact)

                json
            }
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ModAuthor {
            return if (json.isJsonPrimitive) {
                ModAuthor(json.asString, mapOf())
            } else if (json.isJsonObject) {
                val json = json.asJsonObject
                ModAuthor(
                    json.getAsJsonPrimitive("name").asString,
                    json.getAsJsonObject("contact")?.asMap()?.mapValues { it.value.asString } ?: mapOf()
                )
            } else {
                throw JsonParseException("Mod author must be either a string or an object")
            }
        }
    }
}
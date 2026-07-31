package net.typho.big_shot_lib.common.class_patcher

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.reflect.TypeToken

abstract class ModifiersCPEntry(
    @JvmField
    val cls: String,
    @JvmField
    val add: List<Int>,
    @JvmField
    val remove: List<Int>
) : ClassPatcherEntry {
    abstract override val type: Type<*>

    fun apply(access: Int): Int {
        var access = access

        add.forEach { access = access or it }
        remove.forEach { access = access and it.inv() }

        return access
    }

    interface Type<E : ModifiersCPEntry> : ClassPatcherEntry.Type<E> {
        override fun serialize(
            src: E,
            typeOfSrc: java.lang.reflect.Type,
            context: JsonSerializationContext
        ): JsonObject {
            val json = JsonObject()
            json.addProperty("class", src.cls)
            json.add("add", context.serialize(src.add))
            json.add("remove", context.serialize(src.remove))
            return json
        }

        fun create(
            cls: String,
            add: List<Int>,
            remove: List<Int>,
            extra: JsonObject
        ): E

        override fun deserialize(
            json: JsonElement,
            typeOfT: java.lang.reflect.Type,
            context: JsonDeserializationContext
        ): E {
            val json = json.asJsonObject
            return create(
                json.getAsJsonPrimitive("class").asString.replace('.', '/'),
                context.deserialize(json.get("add"), TypeToken.getParameterized(List::class.java, Int::class.java).type) ?: listOf(),
                context.deserialize(json.get("remove"), TypeToken.getParameterized(List::class.java, Int::class.java).type) ?: listOf(),
                json
            )
        }
    }
}
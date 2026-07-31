package net.typho.big_shot_lib.common.class_patcher

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.objectweb.asm.ClassVisitor

interface ClassPatcherEntry {
    companion object {
        @JvmField
        val TYPES = mutableMapOf<String, Type<*>>()
        @JvmField
        val GSON = GsonBuilder()
            .registerTypeAdapter(ClassPatcherEntry::class.java, JsonCodec)
            .create()

        init {
            register(ClassModifiersCPEntry.Type)
            register(MethodModifiersCPEntry.Type)
            register(FieldModifiersCPEntry.Type)
        }

        @JvmStatic
        fun register(type: Type<*>) {
            val old = TYPES.put(type.id, type)

            if (old != type) {
                throw IllegalStateException("Duplicate class patcher entry type ${type.id}: $type and $old")
            }
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <E : ClassPatcherEntry> createVisitor(type: Type<E>, entries: List<ClassPatcherEntry>, api: Int, parent: ClassVisitor?): ClassVisitor? {
            return type.createVisitor(entries as List<E>, api, parent)
        }

        @JvmStatic
        fun createVisitor(entries: List<ClassPatcherEntry>, api: Int, parent: ClassVisitor?): ClassVisitor? {
            return entries.groupBy { it.type }.entries.fold(parent) { visitor, entry -> createVisitor(entry.key, entry.value, api, visitor) ?: visitor }
        }
    }

    val type: Type<*>

    object JsonCodec : JsonSerializer<ClassPatcherEntry>, JsonDeserializer<ClassPatcherEntry> {
        override fun serialize(
            src: ClassPatcherEntry,
            typeOfSrc: java.lang.reflect.Type,
            context: JsonSerializationContext
        ): JsonObject {
            val json = JsonObject()
            json.addProperty("type", src.type.id)

            @Suppress("UNCHECKED_CAST")
            fun <E : ClassPatcherEntry> genericsUgh(type: Type<E>) {
                json.asMap().putAll(type.serialize(src as E, typeOfSrc, context).asJsonObject.asMap())
            }

            genericsUgh(src.type)

            return json
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: java.lang.reflect.Type,
            context: JsonDeserializationContext
        ): ClassPatcherEntry {
            val json = json.asJsonObject
            val typeId = json.getAsJsonPrimitive("type").asString
            val type = TYPES[typeId] ?: throw NullPointerException("Nonexistent class patcher entry type '$typeId'")
            return type.deserialize(json, typeOfT, context)
        }
    }

    interface Type<E : ClassPatcherEntry> : JsonSerializer<E>, JsonDeserializer<E> {
        val id: String
        val cls: Class<E>

        fun createVisitor(entries: List<E>, api: Int, parent: ClassVisitor?): ClassVisitor?
    }
}
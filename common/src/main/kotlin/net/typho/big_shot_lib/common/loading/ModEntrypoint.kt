package net.typho.big_shot_lib.common.loading

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Modifier
import java.lang.reflect.Type

data class ModEntrypoint(
    @JvmField
    val path: String
) {
    object JsonCodec : JsonSerializer<ModEntrypoint>, JsonDeserializer<ModEntrypoint> {
        override fun serialize(
            src: ModEntrypoint,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonPrimitive(src.path)
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ModEntrypoint {
            return ModEntrypoint(json.asString)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> resolveClass(entrypointType: Class<T>): Class<out T> {
        val actual = Class.forName(path)

        if (entrypointType.isAssignableFrom(actual)) {
            return actual as Class<out T>
        } else {
            throw ClassCastException("Expected entrypoint $path to extend $entrypointType, but it does not.")
        }
    }

    @JvmOverloads
    @Suppress("UNCHECKED_CAST")
    fun <T> getOrConstruct(
        entrypointType: Class<T>,
        args: Map<Class<*>, Any?> = mapOf()
    ): T {
        val type = resolveClass(entrypointType)

        type.kotlin.objectInstance?.let { return it }

        try {
            val instance = type.getMethod("getInstance")

            if (Modifier.isStatic(instance.modifiers) && type.isAssignableFrom(instance.returnType)) {
                instance.isAccessible = true
                return instance.invoke(null) as T
            }
        } catch (ignored: NoSuchMethodException) {
        }

        try {
            val instance = type.getField("INSTANCE")

            if (Modifier.isStatic(instance.modifiers) && type.isAssignableFrom(instance.type)) {
                instance.isAccessible = true
                return instance.get(null) as T
            }
        } catch (ignored: NoSuchFieldException) {
        }

        for (constructor in type.constructors) {
            val entries = constructor.parameters.map { expected -> args.entries.find { actual -> expected.type.isAssignableFrom(actual.key) } }

            if (entries.none { it == null }) {
                constructor.isAccessible = true
                return constructor.newInstance(*entries.map { it!!.value }.toTypedArray()) as T
            }
        }

        throw NoSuchMethodException("Could not construct entrypoint $path for type $entrypointType. It has no kotlin object instance, no static getInstance() method, no static INSTANCE field, and no valid constructor for the available arguments ${args.keys}.")
    }
}

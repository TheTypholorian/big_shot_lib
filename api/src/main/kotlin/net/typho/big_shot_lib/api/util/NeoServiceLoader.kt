package net.typho.big_shot_lib.api.util

import com.google.gson.JsonParser
import net.typho.big_shot_lib.api.error.ServiceLoaderException
import kotlin.reflect.KVisibility

object NeoServiceLoader {
    private val services: Map<String, Set<String>> = NeoServiceLoader::class.java.classLoader.getResources("neo_services.json")
        .toList()
        .map { url ->
            val file = url.openStream().use { String(it.readBytes()) }
            val json = JsonParser.parseString(file).asJsonObject
            return@map json.asMap().mapValues { (key, value) -> value.asJsonArray.map { it.asString }.toSet() }
        }
        .fold(HashMap<String, MutableSet<String>>()) { acc, map ->
            map.forEach { (key, value) ->
                acc.computeIfAbsent(key) { HashSet() }.addAll(value)
            }
            return@fold acc
        }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> load(cls: Class<T>): List<T> {
        return services[cls.name]?.map {
            val service = Class.forName(it)

            if (!cls.isAssignableFrom(service)) {
                throw ServiceLoaderException("Class $service does not extend $cls")
            }

            service.kotlin.objectInstance?.let { obj -> return@map obj as T }

            val constructor = service.kotlin.constructors.firstOrNull { constructor ->
                constructor.parameters.isEmpty() && constructor.visibility == KVisibility.PUBLIC
            } ?: throw ServiceLoaderException("Class $service does not have a public no-arg constructor")

            return@map constructor.call() as T
        } ?: listOf()
    }
}
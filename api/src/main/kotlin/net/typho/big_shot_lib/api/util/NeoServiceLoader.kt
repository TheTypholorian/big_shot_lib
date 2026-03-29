package net.typho.big_shot_lib.api.util

import com.google.gson.JsonParser
import net.typho.big_shot_lib.api.error.ServiceLoaderException
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.jvm.jvmName

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
            try {
                val service = Class.forName(it)

                if (!cls.isAssignableFrom(service)) {
                    throw ServiceLoaderException("Class $service does not extend $cls")
                }

                service.kotlin.objectInstance?.let { obj -> return@map obj as T }

                val constructor = service.kotlin.constructors.firstOrNull { constructor ->
                    constructor.parameters.isEmpty() && constructor.visibility == KVisibility.PUBLIC
                } ?: throw ServiceLoaderException("Class $service does not have a public no-arg constructor")

                return@map constructor.call() as T
            } catch (t: Throwable) {
                throw ServiceLoaderException("Error loading service $it for ${cls.name}", t)
            }
        } ?: listOf()
    }

    @JvmStatic
    fun <T : Any> KClass<T>.loadService(): T = load(java).firstOrNull() ?: throw IllegalStateException("Could not find service implementation for $jvmName")

    @JvmStatic
    fun <T : Any> KClass<T>.loadServices(): List<T> = load(java)
}
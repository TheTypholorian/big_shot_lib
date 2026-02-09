package net.typho.big_shot_lib.api.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import java.util.*

abstract class ResourceRegistry<T>(
    val name: ResourceLocation,
    val idConverter: FileToIdConverter,
) {
    companion object {
        @JvmField
        val registries = LinkedList<ResourceRegistry<*>>()
    }

    val map: BiMap<ResourceLocation, T> = HashBiMap.create()

    constructor(name: String) : this(BigShotModUtil.id(name), FileToIdConverter.json("neo/$name"))

    init {
        registries.add(this)
    }

    fun getKey(t: T) = map.inverse()[t]

    fun get(id: ResourceLocation) = map[id]

    abstract fun decode(id: ResourceLocation, json: JsonObject, manager: ResourceManager): T

    fun getReloadListener() = ResourceUtil.INSTANCE.createSimpleResourceReloader(this::reload)

    fun reload(manager: ResourceManager) {
        map.values.forEach { value -> if (value is AutoCloseable) value.close() }
        map.clear()

        for (entry in idConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use { jsonReader ->
                val id = idConverter.fileToId(entry.key)
                val json = JsonParser.parseReader(jsonReader).asJsonObject

                map.put(id, decode(id, json, manager))
            }
        }

        BigShotModUtil.LOGGER.info("Loaded ${map.size} entries of resource registry $name")
    }
}
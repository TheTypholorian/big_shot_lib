package net.typho.big_shot_lib.api.util.resources

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.BigShotApi
import java.util.*

abstract class ResourceRegistry<T>(
    @JvmField
    val name: ResourceIdentifier,
    @JvmField
    val idConverter: NeoFileToIdConverter,
) : NeoResourceManagerReloadListener {
    companion object {
        @JvmField
        val registries = LinkedList<ResourceRegistry<*>>()
    }

    @JvmField
    val map: BiMap<ResourceIdentifier, T> = HashBiMap.create()
    @JvmField
    val lookupCodec: Codec<T> = ResourceIdentifier.CODEC.xmap(this::get, this::getKey)

    init {
        registries.add(this)
    }

    fun getKey(t: T) = map.inverse()[t]

    fun get(id: ResourceIdentifier) = map[id]

    abstract fun decode(id: ResourceIdentifier, json: JsonObject, manager: NeoResourceManager): T

    override fun onResourceManagerReload(manager: NeoResourceManager) {
        map.values.forEach { value -> if (value is AutoCloseable) value.close() }
        map.clear()

        for (entry in idConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use { jsonReader ->
                val id = idConverter.fileToId(entry.key)
                val json = JsonParser.parseReader(jsonReader).asJsonObject

                map.put(id, decode(id, json, manager))
            }
        }

        BigShotApi.LOGGER.info("Loaded ${map.size} entries of resource registry $name")
    }
}
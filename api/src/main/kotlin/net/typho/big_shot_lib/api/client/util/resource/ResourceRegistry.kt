package net.typho.big_shot_lib.api.client.util.resource

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoFileToIdConverter
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.BufferedReader
import java.util.*

abstract class ResourceRegistry<T>(
    override val location: NeoIdentifier,
    @JvmField
    val idConverter: NeoFileToIdConverter,
) : NeoResourceManagerReloadListener, NamedResource {
    companion object {
        @JvmField
        val registries = LinkedList<ResourceRegistry<*>>()
    }

    @JvmField
    val map: BiMap<NeoIdentifier, T> = HashBiMap.create()
    @JvmField
    val lookupCodec: Codec<T> = NeoIdentifier.CODEC.xmap(this::get, this::getKey)

    init {
        registries.add(this)
    }

    fun getKey(t: T) = map.inverse()[t]

    fun get(location: NeoIdentifier) = map[location]

    abstract fun decode(location: NeoIdentifier, reader: BufferedReader, manager: NeoResourceManager): T

    override fun onResourceManagerReload(manager: NeoResourceManager) {
        map.values.forEach { value -> if (value is AutoCloseable) value.close() }
        map.clear()

        for (entry in idConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use {
                val id = idConverter.fileToId(entry.key)
                map.put(id, decode(id, it, manager))
            }
        }

        BigShotApi.LOGGER.info("Loaded ${map.size} entries of resource registry $location")
    }

    abstract class Json<T>(location: NeoIdentifier, idConverter: NeoFileToIdConverter) : ResourceRegistry<T>(location, idConverter) {
        abstract fun decode(location: NeoIdentifier, json: JsonElement, manager: NeoResourceManager): T

        final override fun decode(location: NeoIdentifier, reader: BufferedReader, manager: NeoResourceManager): T {
            return decode(location, JsonParser.parseReader(reader), manager)
        }
    }
}
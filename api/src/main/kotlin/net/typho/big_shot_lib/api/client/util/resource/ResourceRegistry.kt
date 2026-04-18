package net.typho.big_shot_lib.api.client.util.resource

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.util.resource.NeoFileToIdConverter
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.BufferedReader
import java.util.*

abstract class ResourceRegistry<T>(
    override val location: NeoIdentifier,
    @JvmField
    val idConverter: NeoFileToIdConverter,
) : NeoResourceManagerReloadListener {
    companion object {
        @JvmField
        val registries = LinkedList<ResourceRegistry<*>>()
    }

    var map: BiMap<NeoIdentifier, T> = HashBiMap.create()
        protected set
    @JvmField
    val lookupCodec: Codec<T> = NeoIdentifier.CODEC.xmap(this::get, this::getKey)

    init {
        registries.add(this)
    }

    fun getKey(t: T) = map.inverse()[t]

    operator fun get(location: NeoIdentifier) = map[location]

    abstract fun decode(location: NeoIdentifier, reader: BufferedReader, manager: NeoResourceManager): DataResult<T>

    override fun onResourceManagerReload(manager: NeoResourceManager) {
        val oldMap = map
        map = HashBiMap.create()

        for (entry in idConverter.listMatchingResources(manager)) {
            entry.value.openAsReader().use { reader ->
                val id = idConverter.fileToId(entry.key)

                decode(id, reader, manager).mapOrElse(
                    {
                        map[id] = it
                    },
                    { error ->
                        BigShotApi.LOGGER.error("Error loading $id of resource registry $location: ${error.message()}")
                        oldMap.remove(id)?.let { map[id] = it }
                    }
                )
            }
        }

        BigShotApi.LOGGER.info("Loaded ${map.size} entries of resource registry $location")

        oldMap.values.forEach { value -> if (value is AutoCloseable) value.close() }
    }
}
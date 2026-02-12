package net.typho.big_shot_lib.api.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import java.util.*

abstract class ResourceRegistry<T>(
    val name: Identifier,
    val idConverter: FileToIdConverter,
) {
    companion object {
        @JvmField
        val registries = LinkedList<ResourceRegistry<*>>()
    }

    @JvmField
    val map: BiMap<Identifier, T> = HashBiMap.create()
    @JvmField
    val lookupCodec: Codec<T> = Identifier.CODEC.xmap(this::get, this::getKey)

    constructor(name: String) : this(BigShotModUtil.id(name), FileToIdConverter.json("neo/$name"))

    init {
        registries.add(this)
    }

    fun getKey(t: T) = map.inverse()[t]

    fun get(id: Identifier) = map[id]

    abstract fun decode(id: Identifier, json: JsonObject, manager: ResourceManager): T

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
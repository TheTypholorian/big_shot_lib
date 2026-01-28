package net.typho.big_shot_lib.resource

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import net.minecraft.core.*
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.typho.big_shot_lib.util.RecordMapEntry
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

open class ResourceRegistry<T>(
    val key: ResourceKey<Registry<T>>,
    val codec: Codec<T>
) : Registry<T> {
    protected val values: BiMap<ResourceKey<T?>, T> = HashBiMap.create()
    protected val tags: BiMap<TagKey<T?>, Set<ResourceKey<T?>>> = HashBiMap.create()
    protected val ids: MutableList<T> = LinkedList()

    constructor(location: ResourceLocation, codec: Codec<T>) : this(ResourceKey.createRegistryKey(location), codec)

    fun getTags(value: T): Set<TagKey<T?>> {
        val key = values.inverse()[value]
        return tags.entries.stream()
            .filter { tagEntry -> tagEntry.value.contains(key) }
            .map { entry -> entry.key }
            .collect(Collectors.toSet())
    }

    override fun key() = key

    override fun getKey(p0: T) = values.inverse()[p0]?.location()

    override fun getResourceKey(p0: T): Optional<ResourceKey<T?>> = Optional.ofNullable(values.inverse()[p0])

    override fun getId(p0: T?) = ids.indexOf(p0)

    override fun get(p0: ResourceKey<T?>?) = values[p0!!]

    override fun get(p0: ResourceLocation?) = values.entries.firstOrNull { entry -> entry.key.location() == p0 }?.value

    override fun registrationInfo(p0: ResourceKey<T>): Optional<RegistrationInfo> = Optional.of(RegistrationInfo.BUILT_IN)

    override fun registryLifecycle(): Lifecycle = Lifecycle.stable()

    override fun getAny(): Optional<Holder.Reference<T?>> = Optional.ofNullable(values.entries.firstOrNull())
        .map { entry ->
            KeyValueHolderReference(
                holderOwner(),
                entry.key,
                entry.value,
                getTags(entry.value)
            )
        }

    override fun keySet(): Set<ResourceLocation?> = values.keys.stream()
        .map { key -> key?.location() }
        .collect(Collectors.toSet())

    override fun entrySet(): Set<Map.Entry<ResourceKey<T?>, T>> = values.entries.stream()
        .map { entry -> RecordMapEntry(entry.key, entry.value) }
        .collect(Collectors.toSet())

    override fun registryKeySet(): Set<ResourceKey<T?>> = values.keys

    override fun getRandom(p0: RandomSource): Optional<Holder.Reference<T>> {
        return if (ids.isEmpty()) {
            Optional.empty()
        } else {
            Optional.of<Holder.Reference<T>>(createIntrusiveHolder(ids[p0.nextInt(ids.size)]))
        }
    }

    override fun containsKey(p0: ResourceLocation): Boolean = values.keys.any { key -> key.location() == p0 }

    override fun containsKey(p0: ResourceKey<T?>): Boolean = values.containsKey(p0)

    override fun freeze(): Registry<T> = this

    override fun createIntrusiveHolder(p0: T): Holder.Reference<T?> = KeyValueHolderReference(
        holderOwner(),
        getResourceKey(p0).orElseThrow(),
        p0,
        getTags(p0)
    )

    override fun getHolder(p0: Int): Optional<Holder.Reference<T?>> = Optional.of(createIntrusiveHolder(ids[p0]))

    override fun getHolder(p0: ResourceLocation): Optional<Holder.Reference<T?>> = Optional.of(createIntrusiveHolder(get(p0)!!))

    override fun getHolder(p0: ResourceKey<T?>): Optional<Holder.Reference<T?>> = Optional.of(createIntrusiveHolder(get(p0)!!))

    override fun wrapAsHolder(p0: T): Holder<T?> = createIntrusiveHolder(p0)

    override fun holders(): Stream<Holder.Reference<T?>?> = values.entries.stream()
        .map { entry ->
            KeyValueHolderReference(
                holderOwner(),
                entry.key,
                entry.value,
                getTags(entry.value)
            )
        }

    override fun getTag(p0: TagKey<T?>): Optional<HolderSet.Named<T?>?> {
        TODO("Not yet implemented")
    }

    override fun getOrCreateTag(p0: TagKey<T?>): HolderSet.Named<T?> {
        TODO("Not yet implemented")
    }

    override fun getTags(): Stream<Pair<TagKey<T?>?, HolderSet.Named<T?>?>?> {
        TODO("Not yet implemented")
    }

    override fun getTagNames(): Stream<TagKey<T?>?> {
        TODO("Not yet implemented")
    }

    override fun resetTags() {
        TODO("Not yet implemented")
    }

    override fun bindTags(p0: Map<TagKey<T?>?, List<Holder<T?>?>?>) {
        TODO("Not yet implemented")
    }

    override fun holderOwner(): HolderOwner<T?> {
        TODO("Not yet implemented")
    }

    override fun asLookup(): HolderLookup.RegistryLookup<T?> {
        TODO("Not yet implemented")
    }

    override fun byId(p0: Int): T? {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<T?> {
        TODO("Not yet implemented")
    }
}
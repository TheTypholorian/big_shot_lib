package net.typho.big_shot_lib.api.event

import com.mojang.serialization.Lifecycle
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.typho.big_shot_lib.api.util.content.RegisteredObject
import net.typho.big_shot_lib.api.util.resource.NeoReloadListener
import java.util.Optional
import java.util.function.Consumer

interface BrewingStandAccess {
    val numSlots: Int

    operator fun get(index: Int): ItemStack

    operator fun set(index: Int, stack: ItemStack)
}

interface EnchantmentLevelAccess {
    fun get(): Int

    fun set(level: Int)
}

interface ItemEntityExtraLifespanAccess {
    fun get(): Int

    fun set(ticks: Int)

    fun setSeconds(seconds: Float) = set((seconds * 20).toInt())

    fun setMinutes(minutes: Float) = setSeconds(minutes * 60)

    fun add(ticks: Int)

    fun addSeconds(seconds: Float) = add((seconds * 20).toInt())

    fun addMinutes(minutes: Float) = addSeconds(minutes * 60)
}

interface NewRegistryContext {
    fun <T : Any> register(builder: RegistryBuilder<T>): Registry<T>

    fun <T : Any> register(registry: WritableRegistry<T>): WritableRegistry<T>
}

interface RegistryWriter<T : Any> {
    val key: ResourceKey<out Registry<T>>

    fun register(key: Identifier, value: T)

    fun register(key: ResourceKey<out T>, value: T)

    fun <V : T> register(obj: RegisteredObject<V>) {
        when (obj) {
            is RegisteredObject.Immediate<V> -> {
                register(obj.key, obj.get())
            }
            is RegisteredObject.Late<V> -> {
                if (obj.isRegistered()) {
                    register(obj.key, obj.get())
                } else {
                    val value = obj.constructor.invoke()
                    register(obj.key, value)
                    obj.value = value
                }
            }
        }
    }
}

interface RegisterContext {
    fun <T : Any> begin(key: Identifier, out: Consumer<RegistryWriter<T>>)

    fun <T : Any> begin(key: ResourceKey<out Registry<T>>, out: Consumer<RegistryWriter<T>>)

    fun beginBlocks(out: Consumer<RegistryWriter<Block>>) = begin(Registries.BLOCK, out)

    fun beginItems(out: Consumer<RegistryWriter<Item>>) = begin(Registries.ITEM, out)

    open class ToRegistry<V : Any>(
        @JvmField
        protected val registry: WritableRegistry<V>
    ) : RegisterContext {
        var count = 0
            protected set

        override fun <T : Any> begin(
            key: Identifier,
            out: Consumer<RegistryWriter<T>>
        ) {
            begin(ResourceKey.createRegistryKey(key), out)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> begin(
            key: ResourceKey<out Registry<T>>,
            out: Consumer<RegistryWriter<T>>
        ) {
            if (key == registry.key()) {
                val info = RegistrationInfo(Optional.empty(), Lifecycle.stable())
                out.accept(object : RegistryWriter<T> {
                    override val key: ResourceKey<out Registry<T>>
                        get() = key

                    override fun register(key: Identifier, value: T) {
                        registry.register(ResourceKey.create(registry.key(), key), value as V, info)
                        count++
                    }

                    override fun register(key: ResourceKey<out T>, value: T) {
                        registry.register(key as ResourceKey<V>, value as V, info)
                        count++
                    }
                })
            }
        }
    }
}

/**
 * Called when a player picks up a potion from a brewing stand.
 */
fun interface PlayedBrewedPotionEvent {
    operator fun invoke(player: Player, potion: ItemStack)
}

/**
 * Called before a brewing recipe starts.
 */
fun interface PotionPreBrewEvent {
    /**
     * @return If to cancel the recipe.
     */
    operator fun invoke(access: BrewingStandAccess): Boolean
}

/**
 * Called when a brewing recipe finishes.
 */
fun interface PotionPostBrewEvent {
    operator fun invoke(access: BrewingStandAccess)
}

/**
 * Register custom brewing recipes.
 */
fun interface RegisterBrewingRecipesEvent {
    operator fun invoke(builder: PotionBrewing.Builder, registryAccess: RegistryAccess)
}

/**
 * Called when a block's loot table checks the level of an enchantment on the tool to determine what to drop.
 *
 * Example for usage: A custom tool that acts like it has silk touch without needing the enchantment.
 */
fun interface EnchantedBlockLootEvent {
    operator fun invoke(level: LevelAccessor, pos: BlockPos, state: BlockState, tool: ItemInstance, enchantment: Holder<Enchantment>, enchantmentLevel: EnchantmentLevelAccess)
}

/**
 * Called when an entity's loot table checks the level of an enchantment on the tool to determine what to drop.
 *
 * Example for usage: A custom tool that acts like it has fire aspect without needing the enchantment.
 */
fun interface EnchantedEntityLootEvent {
    operator fun invoke(targetEntity: LivingEntity, damageSource: DamageSource, enchantment: Holder<Enchantment>, enchantmentLevel: EnchantmentLevelAccess)
}

/**
 * Called when the level of a particular enchantment (or if the enchantment parameter is null, all enchantments) on an item is needed for gameplay purposes.
 *
 * Does not affect the tooltip nor NBT.
 */
fun interface GetEnchantmentLevelEvent {
    operator fun invoke(item: ItemInstance, enchantments: ItemEnchantments.Mutable, lookup: HolderLookup.RegistryLookup<Enchantment>, enchantment: Holder<Enchantment>?)
}

/**
 * Called when a dropped item is about to despawn. You can add extra lifespan with this event.
 */
fun interface ItemEntityDespawnEvent {
    operator fun invoke(item: ItemEntity, extraLife: ItemEntityExtraLifespanAccess)
}

/**
 * Called when a player drops an item.
 */
fun interface DropItemEvent {
    /**
     * @return If to cancel the item being spawned in the world (does NOT prevent it from being removed from the inventory).
     */
    operator fun invoke(item: ItemEntity, player: Player): Boolean
}

// TODO neoforge entity.living, entity.player, entity, furnace, level.block, level, server, tick, village, root events

/**
 * Register custom server-side data reload listeners.
 */
fun interface RegisterServerReloadListenersEvent {
    operator fun invoke(out: Consumer<NeoReloadListener>)
}

/**
 * Called when a chunk is loaded.
 */
fun interface ChunkLoadedEvent {
    operator fun invoke(level: LevelAccessor, chunk: ChunkAccess)
}

/**
 * Called when a chunk is unloaded.
 */
fun interface ChunkUnloadedEvent {
    operator fun invoke(level: LevelAccessor, chunk: ChunkAccess)
}

/**
 * Use this to create new registries.
 */
fun interface NewRegistryEvent {
    operator fun invoke(context: NewRegistryContext)
}

/**
 * Use this to register content, such as blocks, items, etc.
 */
fun interface RegisterEvent {
    operator fun invoke(context: RegisterContext)
}

// custom events

/**
 * Called when a block state is changed.
 */
fun interface BlockStateChangedEvent {
    operator fun invoke(level: LevelAccessor, pos: BlockPos, oldState: BlockState, newState: BlockState)
}
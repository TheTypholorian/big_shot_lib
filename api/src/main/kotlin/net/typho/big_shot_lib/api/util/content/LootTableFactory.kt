package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import net.typho.big_shot_lib.api.BigShotLibMod
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterEvent

@Suppress("UNCHECKED_CAST")
open class LootTableFactory(
    mod: BigShotLibMod
) : ContentFactory<LootTable> {
    override val registry: ResourceKey<Registry<LootTable>> = Registries.LOOT_TABLE
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashSetOf<RegisteredObject.Late<LootTable>>()

    init {
        mod.addListener { bus ->
            bus.register(RegisterEvent { out ->
                out.begin(registry) { out ->
                    registered = true

                    toRegister.forEach { out.register(it) }
                }
            })
        }
    }

    override fun begin(key: Identifier): Builder<*> {
        return BuilderImpl(ResourceKey.create(registry, key), this)
    }

    private class BuilderImpl(
        key: ResourceKey<LootTable>,
        parent: LootTableFactory
    ) : Builder<BuilderImpl>(key, parent)

    open class Builder<B : Builder<B>>(
        @JvmField
        val key: ResourceKey<LootTable>,
        @JvmField
        protected val parent: LootTableFactory
    ) : ObjectBuilder<RegisteredObject<LootTable>> {
        @JvmField
        protected val mutators = arrayListOf<(builder: LootTable.Builder) -> LootTable.Builder>()

        fun withPool(pool: () -> LootPool.Builder): B {
            mutators.add { it.withPool(pool()) }
            return this as B
        }

        fun setParamSet(paramSet: () -> LootContextParamSet): B {
            mutators.add { it.setParamSet(paramSet()) }
            return this as B
        }

        fun setRandomSequence(randomSequence: () -> Identifier): B {
            mutators.add { it.setRandomSequence(randomSequence()) }
            return this as B
        }

        fun apply(function: () -> LootItemFunction.Builder): B {
            mutators.add { it.apply(function()) }
            return this as B
        }

        override fun end(): RegisteredObject<LootTable> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val obj = RegisteredObject.Late(key) { mutators.fold(LootTable.Builder()) { builder, mutator -> mutator(builder) }.build() }

            if (!parent.toRegister.add(obj)) {
                throw IllegalArgumentException("Cannot create two advancements under the same ID $key")
            }

            return obj
        }
    }
}
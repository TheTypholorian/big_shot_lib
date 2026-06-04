package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterEvent

@Suppress("UNCHECKED_CAST")
open class LootTableContentFactory<O : NeoEventBus> protected constructor() : ContentFactory<RegisteredObject<LootTable>, ResourceKey<LootTable>, O> {
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashSetOf<RegisteredObject.Late<LootTable>>()

    companion object {
        @JvmStatic
        @JvmName("simple")
        operator fun invoke(): LootTableContentFactory<NeoEventBus> {
            return LootTableContentFactory<NeoEventBus>()
        }
    }

    open fun begin(key: Identifier): Builder<*> {
        return begin(ResourceKey.create(Registries.LOOT_TABLE, key))
    }

    override fun begin(key: ResourceKey<LootTable>): Builder<*> {
        return BuilderImpl(key, this)
    }

    override fun end(output: O) {
        registered = true

        output.register(RegisterEvent { out ->
            out.begin(Registries.LOOT_TABLE) { out ->
                toRegister.forEach { out.register(it) }
            }
        })
    }

    private class BuilderImpl(
        key: ResourceKey<LootTable>,
        parent: LootTableContentFactory<*>
    ) : Builder<BuilderImpl>(key, parent)

    open class Builder<B : Builder<B>>(
        @JvmField
        val key: ResourceKey<LootTable>,
        @JvmField
        protected val parent: LootTableContentFactory<*>
    ) : ObjectBuilder<RegisteredObject<LootTable>> {
        @JvmField
        protected val mutators = arrayListOf<(builder: LootTable.Builder) -> LootTable.Builder>()

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
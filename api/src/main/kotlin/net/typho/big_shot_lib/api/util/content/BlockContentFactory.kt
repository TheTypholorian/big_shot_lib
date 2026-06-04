package net.typho.big_shot_lib.api.util.content

import net.minecraft.client.color.block.BlockColor
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.UnaryOperator
import kotlin.collections.addAll

@Suppress("UNCHECKED_CAST")
open class BlockContentFactory @JvmOverloads constructor(
    @JvmField
    protected val items: ItemContentFactory? = null,
    @JvmField
    protected val loot: LootTableContentFactory? = null
) : ContentFactory<Block> {
    override val registry: ResourceKey<Registry<Block>> = Registries.BLOCK
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashMapOf<ResourceKey<out Block>, RegisteredObject<out Block>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<out Block>, MutableSet<ResourceKey<out Block>>>()

    open fun begin(key: Identifier): Builder<Block, *> {
        return begin(ResourceKey.create(registry, key))
    }

    override fun begin(key: ResourceKey<Block>): Builder<Block, *> {
        return beginComplex(key)
    }

    open fun <V : Block> beginComplex(key: Identifier): Builder<V, *> {
        return beginComplex(ResourceKey.create(registry, key) as ResourceKey<V>)
    }

    open fun <V : Block> beginComplex(key: ResourceKey<V>): Builder<V, *> {
        return BuilderImpl(key, this)
    }

    override fun end(bus: NeoEventBus) {
        bus.register(RegisterEvent { out ->
            out.beginBlocks { out ->
                registered = true

                toRegister.values.forEach { out.register(it) }
            }
        })
        bus.register(RegisterDynamicTagsEvent { out ->
            registered = true

            dynamicTags.forEach { (key, value) -> out.addBlocks(key, *value.toTypedArray()) }
        })
    }

    private class ClientInfoImpl : ClientInfo<ClientInfoImpl>()

    open class ClientInfo<B : ClientInfo<B>> {
        @JvmField
        protected var renderType: NeoRenderType = NeoRenderType.BUILTINS.solid

        fun renderType(renderType: NeoRenderType): B {
            this.renderType = renderType
            return this as B
        }
    }

    private class BuilderImpl<T : Block>(
        key: ResourceKey<T>,
        parent: BlockContentFactory
    ) : Builder<T, BuilderImpl<T>>(key, parent)

    open class Builder<T : Block, B : Builder<T, B>>(
        @JvmField
        val key: ResourceKey<T>,
        @JvmField
        protected val parent: BlockContentFactory
    ) : ObjectBuilder<RegisteredObject<T>> {
        @JvmField
        protected var constructor: (properties: BlockBehaviour.Properties) -> T = { properties -> Block(properties) as? T ?: throw ClassCastException("Must specify a constructor for $key as it doesn't use the base Block class.") }
        @JvmField
        protected var properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
        @JvmField
        protected var clientInfo: ClientInfo<*>? = null // TODO do stuff with this
        // TODO block item
        // TODO block entity
        @JvmField
        protected var color: BlockColor? = null
        // TODO model
        @JvmField
        protected var lootTable: BiFunction<T, BlockItem, RegisteredObject<LootTable>>? = parent.loot?.let { loot ->
            BiFunction { block, item ->
                loot.begin(block.lootTable)
                    .withPool {
                        LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .`when`(ExplosionCondition.survivesExplosion())
                            .add(LootItem.lootTableItem(item))
                    }
                    .end()
            }
        }
        // TODO client side extensions
        @JvmField
        protected val tags: MutableList<TagKey<Block>> = arrayListOf()
        @JvmField
        protected var item: Supplier<RegisteredObject<out BlockItem>>? = parent.items?.let { items ->
            Supplier {
                items.beginComplex(key as ResourceKey<BlockItem>)
                    .constructor { properties -> BlockItem(registered!!.get(), properties) }
                    .end()
            }
        }
        @JvmField
        protected var registered: RegisteredObject<T>? = null

        fun properties(properties: BlockBehaviour.Properties.() -> BlockBehaviour.Properties): B {
            this.properties = properties(this.properties)
            return this as B
        }

        fun clientInfo(info: UnaryOperator<ClientInfo<*>>): B {
            if (PlatformUtil.INSTANCE.isClient()) {
                clientInfo = info.apply(clientInfo ?: ClientInfoImpl())
            }

            return this as B
        }

        fun color(color: BlockColor): B {
            this.color = color
            return this as B
        }

        fun constructor(constructor: (properties: BlockBehaviour.Properties) -> T): B {
            this.constructor = constructor
            return this as B
        }

        fun tags(vararg tags: TagKey<Block>): B {
            this.tags.addAll(tags)
            return this as B
        }

        fun noLoot(): B {
            lootTable = null
            return this as B
        }

        @JvmOverloads
        fun item(
            builder: ItemContentFactory.Builder<out BlockItem, *>.() -> ItemContentFactory.Builder<out BlockItem, *>,
            key: ResourceKey<Item> = this.key as ResourceKey<Item>
        ): B {
            item = Supplier {
                (parent.items ?: throw UnsupportedOperationException("Must pass an ItemContentFactory to the BlockContentFactory to be able to call Builder.item()")).beginComplex(key as ResourceKey<BlockItem>)
                    .constructor { BlockItem(registered!!.get(), it) }
                    .let(builder)
                    .end()
            }

            return this as B
        }

        fun drops(
            builder: LootTableContentFactory.Builder<*>.() -> LootTableContentFactory.Builder<*>
        ): B {
            lootTable = BiFunction { block, item ->
                (parent.loot ?: throw UnsupportedOperationException("Must pass a LootTableContentFactory to the BlockContentFactory to be able to call Builder.drops()")).begin(block.lootTable)
                    .let(builder)
                    .end()
            }

            return this as B
        }

        fun noItem(): B {
            item = null
            return this as B
        }

        override fun end(): RegisteredObject<T> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val block = RegisteredObject.Late(key) { constructor(properties) }

            registered = block

            parent.toRegister.put(key, block)?.let { old ->
                throw IllegalArgumentException("Cannot create two blocks ($block and $old) under the same ID $key")
            }

            for (tag in tags) {
                parent.dynamicTags.computeIfAbsent(tag) { hashSetOf() }.add(key)
            }

            item?.get()?.addListener { item ->
                block.addListener { block ->
                    lootTable?.apply(block, item)
                }
            }

            return block
        }
    }
}
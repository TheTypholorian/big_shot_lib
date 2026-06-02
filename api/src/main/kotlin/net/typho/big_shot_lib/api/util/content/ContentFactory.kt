package net.typho.big_shot_lib.api.util.content

import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.color.item.ItemColor
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterEvent
import java.util.function.UnaryOperator

typealias SimpleBlocksContentFactory = ContentFactory.Blocks<Block, NeoEventBus, *>

@Suppress("UNCHECKED_CAST")
interface ContentFactory<T : Any, K, O, B : ContentFactory.ObjectBuilder<out T>> {
    fun <V : T> begin(key: K): B

    fun end(output: O)

    interface ObjectBuilder<T : Any> {
        fun end(): T
    }

    open class Blocks<T : Block, O : NeoEventBus, B : Blocks<T, O, B>.Builder<T>> : ContentFactory<T, Identifier, O, B> {
        @JvmField
        protected var registered = false
        @JvmField
        protected val toRegister = hashMapOf<Identifier, Block>()

        override fun <V : T> begin(key: Identifier): B {
            return begin<V>(key, BlockBehaviour.Properties.of())
        }

        fun <V : T> begin(key: Identifier, copyProperties: BlockBehaviour): B {
            return begin<V>(key, BlockBehaviour.Properties.ofFullCopy(copyProperties))
        }

        fun <V : T> begin(key: Identifier, properties: BlockBehaviour.Properties): B {
            return Builder<V>(key, properties) as? B ?: throw IllegalStateException("ContentFactory.Blocks subclass $this must override begin(Identifier) as it defines a different Builder type")
        }

        override fun end(output: O) {
            output.register(RegisterEvent { out ->
                out.beginBlocks { out ->
                    registered = true

                    for (entry in toRegister) {
                        out.register(entry.key, entry.value)
                    }
                }
            })
        }

        open inner class Builder<V : T>(
            @JvmField
            val key: Identifier,
            @JvmField
            protected var properties: BlockBehaviour.Properties
        ) : ObjectBuilder<V> {
            protected lateinit var constructor: (properties: BlockBehaviour.Properties) -> V
            @JvmField
            protected var renderType: NeoRenderType = NeoRenderType.BUILTINS.solid // TODO
            // TODO block item
            // TODO block entity
            @JvmField
            protected var color: BlockColor? = null
            // TODO model
            // TODO loot
            // TODO recipe
            // TODO client side extensions
            // TODO tags

            fun properties(properties: (BlockBehaviour.Properties) -> BlockBehaviour.Properties): B {
                this.properties = properties(this.properties)
                return this as B
            }

            fun renderType(renderType: NeoRenderType): B {
                this.renderType = renderType
                return this as B
            }

            fun color(color: BlockColor): B {
                this.color = color
                return this as B
            }

            fun constructor(constructor: (properties: BlockBehaviour.Properties) -> V): B {
                this.constructor = constructor
                return this as B
            }

            override fun end(): V {
                if (registered) {
                    throw IllegalStateException("ContentFactory ${this@Blocks} has ended, it cannot receive more entries")
                }

                val block = constructor(properties)

                toRegister.put(key, block)?.let { old ->
                    throw IllegalArgumentException("Cannot create two blocks ($block and $old) under the same ID $key")
                }

                return block
            }
        }
    }

    open class Items<T : Item, O : NeoEventBus, B : Items<T, O, B>.Builder<T>> : ContentFactory<T, Identifier, O, B> {
        @JvmField
        protected var registered = false
        @JvmField
        protected val toRegister = hashMapOf<Identifier, Item>()

        override fun <V : T> begin(key: Identifier): B {
            return begin<V>(key, Item.Properties())
        }

        fun <V : T> begin(key: Identifier, properties: Item.Properties): B {
            return Builder<V>(key, properties) as? B ?: throw IllegalStateException("ContentFactory.Items subclass $this must override begin(Identifier) as it defines a different Builder type")
        }

        override fun end(output: O) {
            output.register(RegisterEvent { out ->
                out.beginItems { out ->
                    registered = true

                    for (entry in toRegister) {
                        out.register(entry.key, entry.value)
                    }
                }
            })
        }

        open inner class Builder<V : T>(
            @JvmField
            val key: Identifier,
            @JvmField
            protected var properties: Item.Properties
        ) : ObjectBuilder<V> {
            protected lateinit var constructor: (properties: Item.Properties) -> V
            // TODO creative tab
            // TODO model
            // TODO recipe
            @JvmField
            protected var burnTime: Int? = null
            @JvmField
            protected var compostable: Float? = null
            // TODO tag
            @JvmField
            protected var renderType: UnaryOperator<NeoRenderType>? = null // TODO

            fun properties(properties: (Item.Properties) -> Item.Properties): B {
                this.properties = properties(this.properties)
                return this as B
            }

            fun renderType(renderType: NeoRenderType): B {
                this.renderType = renderType
                return this as B
            }

            fun constructor(constructor: (properties: Item.Properties) -> V): B {
                this.constructor = constructor
                return this as B
            }

            override fun end(): V {
                if (registered) {
                    throw IllegalStateException("ContentFactory ${this@Items} has ended, it cannot receive more entries")
                }

                val item = constructor(properties)

                toRegister.put(key, item)?.let { old ->
                    throw IllegalArgumentException("Cannot create two items ($item and $old) under the same ID $key")
                }

                return item
            }
        }
    }
}
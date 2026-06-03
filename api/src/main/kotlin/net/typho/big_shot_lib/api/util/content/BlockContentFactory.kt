package net.typho.big_shot_lib.api.util.content

import net.minecraft.client.color.block.BlockColor
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.UnaryOperator

@Suppress("UNCHECKED_CAST")
open class BlockContentFactory<O : NeoEventBus, B : BlockContentFactory.Builder<Block, B>> protected constructor() : ContentFactory<Block, Identifier, O, B> {
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashMapOf<Identifier, RegisteredObject<out Block>>()

    companion object {
        @JvmStatic
        @JvmName("simple")
        operator fun invoke(): BlockContentFactory<NeoEventBus, *> {
            return BlockContentFactory<NeoEventBus, BuilderImpl<Block>>()
        }
    }

    override fun begin(key: Identifier): B {
        return begin(key, BlockBehaviour.Properties.of())
    }

    open fun begin(key: Identifier, copyProperties: BlockBehaviour): B {
        return begin(key, BlockBehaviour.Properties.ofFullCopy(copyProperties))
    }

    open fun begin(key: Identifier, properties: BlockBehaviour.Properties): B {
        return beginComplex<Block>(key, properties)
    }

    override fun <V : Block> beginComplex(key: Identifier): B {
        return beginComplex<V>(key, BlockBehaviour.Properties.of())
    }

    open fun <V : Block> beginComplex(key: Identifier, copyProperties: BlockBehaviour): B {
        return beginComplex<V>(key, BlockBehaviour.Properties.ofFullCopy(copyProperties))
    }

    open fun <V : Block> beginComplex(key: Identifier, properties: BlockBehaviour.Properties): B {
        return BuilderImpl<V>(key, properties, this) as? B ?: throw IllegalStateException("BlockContentFactory subclass $this must override begin(Identifier) as it defines a different Builder type")
    }

    override fun end(output: O) {
        output.register(RegisterEvent { out ->
            out.beginBlocks { out ->
                registered = true

                toRegister.values.forEach { out.register(it) }
            }
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
        key: Identifier,
        properties: BlockBehaviour.Properties,
        parent: BlockContentFactory<*, *>
    ) : Builder<T, BuilderImpl<T>>(key, properties, parent)

    open class Builder<T : Block, B : Builder<T, B>>(
        @JvmField
        val key: Identifier,
        @JvmField
        protected var properties: BlockBehaviour.Properties,
        @JvmField
        protected val parent: BlockContentFactory<*, *>
    ) : ObjectBuilder<T> {
        @JvmField
        protected var constructor: (properties: BlockBehaviour.Properties) -> T = { properties -> Block(properties) as? T ?: throw ClassCastException("Must specify a constructor for $key as it doesn't use the base Block class.") }
        @JvmField
        protected var clientInfo: ClientInfo<*>? = null // TODO do stuff with this
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

        override fun end(): RegisteredObject<T> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val block = RegisteredObject(key) { constructor(properties) }

            parent.toRegister.put(key, block)?.let { old ->
                throw IllegalArgumentException("Cannot create two blocks ($block and $old) under the same ID $key")
            }

            return block
        }
    }
}
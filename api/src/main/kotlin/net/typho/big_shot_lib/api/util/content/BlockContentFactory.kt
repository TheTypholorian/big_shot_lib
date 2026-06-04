package net.typho.big_shot_lib.api.util.content

import net.minecraft.client.color.block.BlockColor
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.UnaryOperator
import kotlin.collections.addAll

@Suppress("UNCHECKED_CAST")
open class BlockContentFactory<O : NeoEventBus> protected constructor(
    @JvmField
    protected val items: ItemContentFactory<*>? = null
) : ContentFactory<Block, Identifier, O> {
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashMapOf<Identifier, RegisteredObject<out Block>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<Block>, MutableSet<Identifier>>()

    companion object {
        @JvmOverloads
        @JvmStatic
        @JvmName("simple")
        operator fun invoke(items: ItemContentFactory<*>? = null): BlockContentFactory<NeoEventBus> {
            return BlockContentFactory<NeoEventBus>(items)
        }
    }

    override fun begin(key: Identifier): Builder<Block, *> {
        return begin(key, BlockBehaviour.Properties.of())
    }

    open fun begin(key: Identifier, copyProperties: BlockBehaviour): Builder<Block, *> {
        return begin(key, BlockBehaviour.Properties.ofFullCopy(copyProperties))
    }

    open fun begin(key: Identifier, properties: BlockBehaviour.Properties): Builder<Block, *> {
        return beginComplex(key, properties)
    }

    fun <V : Block> beginComplex(key: Identifier): Builder<V, *> {
        return beginComplex(key, BlockBehaviour.Properties.of())
    }

    open fun <V : Block> beginComplex(key: Identifier, copyProperties: BlockBehaviour): Builder<V, *> {
        return beginComplex(key, BlockBehaviour.Properties.ofFullCopy(copyProperties))
    }

    open fun <V : Block> beginComplex(key: Identifier, properties: BlockBehaviour.Properties): Builder<V, *> {
        return BuilderImpl(key, properties, this)
    }

    override fun end(output: O) {
        registered = true

        output.register(RegisterEvent { out ->
            out.beginBlocks { out ->
                registered = true

                toRegister.values.forEach { out.register(it) }
            }
        })
        output.register(RegisterDynamicTagsEvent { out ->
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
        key: Identifier,
        properties: BlockBehaviour.Properties,
        parent: BlockContentFactory<*>
    ) : Builder<T, BuilderImpl<T>>(key, properties, parent)

    open class Builder<T : Block, B : Builder<T, B>>(
        @JvmField
        val key: Identifier,
        @JvmField
        protected var properties: BlockBehaviour.Properties,
        @JvmField
        protected val parent: BlockContentFactory<*>
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
        @JvmField
        protected val tags: MutableList<TagKey<Block>> = arrayListOf()
        @JvmField
        protected var item: Runnable? = parent.items?.let {
            Runnable {
                it.beginComplex<BlockItem>(key)
                    .constructor { BlockItem(registered!!.get(), it) }
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

        @JvmOverloads
        fun item(
            builder: ItemContentFactory.Builder<BlockItem, *>.() -> ItemContentFactory.Builder<BlockItem, *> = { this },
            properties: Item.Properties = Item.Properties(),
            key: Identifier = this.key
        ): B {
            parent.items ?: throw NullPointerException("Must pass an ItemContentFactory to the BlockContentFactory to be able to call Builder.item()")

            item = Runnable {
                parent.items.beginComplex<BlockItem>(key, properties)
                    .constructor { BlockItem(registered!!.get(), it) }
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

            item?.run()

            return block
        }
    }
}
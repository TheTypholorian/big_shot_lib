package net.typho.big_shot_lib.api.util.content

import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.content.ItemContentFactory.Builder
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.UnaryOperator

@Suppress("UNCHECKED_CAST")
open class ItemContentFactory<O : NeoEventBus, B : Builder<*, B>> protected constructor() : ContentFactory<Item, Identifier, O, B> {
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashMapOf<Identifier, RegisteredObject<out Item>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<Item>, MutableSet<Identifier>>()

    companion object {
        @JvmStatic
        @JvmName("simple")
        operator fun invoke(): ItemContentFactory<NeoEventBus, *> {
            return ItemContentFactory<NeoEventBus, BuilderImpl<Item>>()
        }
    }

    override fun begin(key: Identifier): B {
        return begin(key, Item.Properties())
    }

    open fun begin(key: Identifier, properties: Item.Properties): B {
        return beginComplex<Item>(key, properties)
    }

    override fun <T : Item> beginComplex(key: Identifier): B {
        return beginComplex<T>(key, Item.Properties())
    }

    open fun <T : Item> beginComplex(key: Identifier, properties: Item.Properties): B {
        return BuilderImpl<T>(key, properties, this) as? B ?: throw IllegalStateException("ItemContentFactory subclass $this must override begin(Identifier) as it defines a different Builder type")
    }

    override fun end(output: O) {
        output.register(RegisterEvent { out ->
            out.beginItems { out ->
                registered = true

                toRegister.values.forEach { out.register(it) }
            }
        })
        output.register(RegisterDynamicTagsEvent { out ->
            dynamicTags.forEach { (key, value) -> out.addItems(key, *value.toTypedArray()) }
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

    private class BuilderImpl<T : Item>(
        key: Identifier,
        properties: Item.Properties,
        parent: ItemContentFactory<*, *>
    ) : Builder<T, BuilderImpl<T>>(key, properties, parent)

    open class Builder<T : Item, B : Builder<T, B>>(
        @JvmField
        val key: Identifier,
        @JvmField
        protected var properties: Item.Properties,
        @JvmField
        protected val parent: ItemContentFactory<*, *>
    ) : ObjectBuilder<T> {
        @JvmField
        protected var constructor: (properties: Item.Properties) -> T = { properties -> Item(properties) as? T ?: throw ClassCastException("Must specify a constructor for $key as it doesn't use the base Item class.") }
        @JvmField
        protected var clientInfo: ClientInfo<*>? = null // TODO do stuff with this
        // TODO creative tab
        // TODO model
        // TODO recipe
        @JvmField
        protected var burnTime: Int? = null
        @JvmField
        protected var compostable: Float? = null
        @JvmField
        protected val tags: MutableList<TagKey<Item>> = arrayListOf()
        // TODO tag

        fun properties(properties: (Item.Properties) -> Item.Properties): B {
            this.properties = properties(this.properties)
            return this as B
        }

        fun clientInfo(info: UnaryOperator<ClientInfo<*>>): B {
            if (PlatformUtil.INSTANCE.isClient()) {
                clientInfo = info.apply(clientInfo ?: ClientInfoImpl())
            }

            return this as B
        }

        fun constructor(constructor: (properties: Item.Properties) -> T): B {
            this.constructor = constructor
            return this as B
        }

        fun tags(vararg tags: TagKey<Item>): B {
            this.tags.addAll(tags)
            return this as B
        }

        override fun end(): RegisteredObject<T> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val item = RegisteredObject(key) { constructor(properties) }

            parent.toRegister.put(key, item)?.let {
                throw IllegalArgumentException("Cannot create two items under the same ID $key")
            }

            for (tag in tags) {
                parent.dynamicTags.computeIfAbsent(tag) { hashSetOf() }.add(key)
            }

            return item
        }
    }
}
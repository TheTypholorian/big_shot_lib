package net.typho.big_shot_lib.api.util.content

import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.UnaryOperator

@Suppress("UNCHECKED_CAST")
open class ItemContentFactory<O : NeoEventBus> protected constructor() : ContentFactory<Item, Identifier, O> {
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashMapOf<Identifier, RegisteredObject<out Item>>()
    @JvmField
    protected val dynamicRecipes = hashMapOf<Identifier, () -> RecipeBuilder>()
    @JvmField
    protected val dynamicExistingRecipes = hashMapOf<Identifier, () -> Recipe<*>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<Item>, MutableSet<Identifier>>()

    companion object {
        @JvmStatic
        @JvmName("simple")
        operator fun invoke(): ItemContentFactory<NeoEventBus> {
            return ItemContentFactory<NeoEventBus>()
        }
    }

    override fun begin(key: Identifier): Builder<Item, *> {
        return begin(key, Item.Properties())
    }

    open fun begin(key: Identifier, properties: Item.Properties): Builder<Item, *> {
        return beginComplex(key, properties)
    }

    fun <T : Item> beginComplex(key: Identifier): Builder<T, *> {
        return beginComplex(key, Item.Properties())
    }

    open fun <T : Item> beginComplex(key: Identifier, properties: Item.Properties): Builder<T, *> {
        return BuilderImpl(key, properties, this)
    }

    override fun end(output: O) {
        registered = true

        output.register(RegisterEvent { out ->
            out.beginItems { out ->
                toRegister.values.forEach { out.register(it) }
            }
        })
        output.register(RegisterDynamicRecipesEvent { out, registries ->
            dynamicRecipes.forEach { (key, value) -> out.register(key, value()) }
            dynamicExistingRecipes.forEach { (key, value) -> out.register(key, value()) }
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
        parent: ItemContentFactory<*>
    ) : Builder<T, BuilderImpl<T>>(key, properties, parent)

    open class Builder<T : Item, B : Builder<T, B>>(
        @JvmField
        val key: Identifier,
        @JvmField
        protected var properties: Item.Properties,
        @JvmField
        protected val parent: ItemContentFactory<*>
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
        @JvmField
        protected var registered: RegisteredObject<T>? = null

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

        @JvmOverloads
        fun recipe(location: Identifier = key, recipe: (item: T) -> RecipeBuilder): B {
            parent.dynamicRecipes.put(location) { recipe(registered!!.get()) }?.let { throw IllegalStateException("Already registered a recipe for $key with id $location") }
            return this as B
        }

        @JvmOverloads
        fun existingRecipe(location: Identifier = key, recipe: (item: T) -> Recipe<*>): B {
            parent.dynamicExistingRecipes.put(location) { recipe(registered!!.get()) }?.let { throw IllegalStateException("Already registered a recipe for $key with id $location") }
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

            val item = RegisteredObject.Late(key) { constructor(properties) }

            registered = item

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
package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.UnaryOperator

@Suppress("UNCHECKED_CAST")
open class ItemContentFactory : ContentFactory<Item> {
    override val registry: ResourceKey<Registry<Item>> = Registries.ITEM
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashMapOf<ResourceKey<out Item>, RegisteredObject<out Item>>()
    @JvmField
    protected val dynamicRecipes = hashMapOf<ResourceKey<out Recipe<*>>, () -> RecipeBuilder>()
    @JvmField
    protected val dynamicExistingRecipes = hashMapOf<ResourceKey<out Recipe<*>>, () -> Recipe<*>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<Item>, MutableSet<ResourceKey<out Item>>>()

    open fun begin(key: Identifier): Builder<Item, *> {
        return begin(ResourceKey.create(registry, key))
    }

    override fun begin(key: ResourceKey<Item>): Builder<Item, *> {
        return beginComplex(key)
    }

    open fun <V : Item> beginComplex(key: Identifier): Builder<V, *> {
        return beginComplex(ResourceKey.create(registry, key) as ResourceKey<V>)
    }

    open fun <V : Item> beginComplex(key: ResourceKey<V>): Builder<V, *> {
        return BuilderImpl(key, this)
    }

    override fun end(bus: NeoEventBus) {
        bus.register(RegisterEvent { out ->
            out.beginItems { out ->
                registered = true

                toRegister.values.forEach { out.register(it) }
            }
        })
        bus.register(RegisterDynamicRecipesEvent { out, registries ->
            registered = true

            dynamicRecipes.forEach { (key, value) -> out.register(key, value()) }
            dynamicExistingRecipes.forEach { (key, value) -> out.register(key, value()) }
        })
        bus.register(RegisterDynamicTagsEvent { out ->
            registered = true

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
        key: ResourceKey<T>,
        parent: ItemContentFactory
    ) : Builder<T, BuilderImpl<T>>(key, parent)

    open class Builder<T : Item, B : Builder<T, B>>(
        @JvmField
        val key: ResourceKey<T>,
        @JvmField
        protected val parent: ItemContentFactory
    ) : ObjectBuilder<RegisteredObject<T>> {
        @JvmField
        protected var constructor: (properties: Item.Properties) -> T = { properties -> Item(properties) as? T ?: throw ClassCastException("Must specify a constructor for $key as it doesn't use the base Item class.") }
        @JvmField
        protected var properties: Item.Properties = Item.Properties()
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

        fun properties(properties: Item.Properties.() -> Item.Properties): B {
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
        fun recipe(location: ResourceKey<out Recipe<*>> = key as ResourceKey<Recipe<*>>, recipe: (item: T) -> RecipeBuilder): B {
            parent.dynamicRecipes.put(location) { recipe(registered!!.get()) }?.let { throw IllegalStateException("Already registered a recipe for $key with id $location") }
            return this as B
        }

        @JvmOverloads
        fun existingRecipe(location: ResourceKey<out Recipe<*>> = key as ResourceKey<Recipe<*>>, recipe: (item: T) -> Recipe<*>): B {
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
package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.Supplier
import java.util.function.UnaryOperator

@Suppress("UNCHECKED_CAST")
open class ItemContentFactory : ContentFactory<Item> {
    override val registry: ResourceKey<Registry<Item>> = Registries.ITEM
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = arrayListOf<RegisteredObject<out Item>>()
    @JvmField
    protected val dynamicRecipes = hashMapOf<ResourceKey<out Recipe<*>>, Supplier<out RecipeBuilder>>()
    @JvmField
    protected val dynamicExistingRecipes = hashMapOf<ResourceKey<out Recipe<*>>, Supplier<out Recipe<*>>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<Item>, MutableSet<ResourceKey<out Item>>>()

    override fun begin(key: Identifier): Builder<Item, *> {
        return beginComplex(key) { Item(it) }
    }

    open fun beginBlockItem(key: Identifier, block: Supplier<out Block>): Builder<BlockItem, *> {
        return beginComplex(key) { BlockItem(block.get(), it) }
    }

    open fun <V : Item> beginComplex(key: Identifier, constructor: (properties: Item.Properties) -> V): Builder<V, *> {
        return BuilderImpl(ResourceKey.create(registry, key) as ResourceKey<V>, constructor, this)
    }

    override fun end(bus: NeoEventBus) {
        bus.register(RegisterEvent { out ->
            out.beginItems { out ->
                registered = true

                toRegister.forEach { out.register(it) }
            }
        })
        bus.register(RegisterDynamicRecipesEvent { out, registries ->
            registered = true

            dynamicRecipes.forEach { (key, value) -> out.register(key, value.get()) }
            dynamicExistingRecipes.forEach { (key, value) -> out.register(key, value.get()) }
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
        constructor: (properties: Item.Properties) -> T,
        parent: ItemContentFactory
    ) : Builder<T, BuilderImpl<T>>(key, constructor, parent)

    open class Builder<T : Item, B : Builder<T, B>>(
        @JvmField
        val key: ResourceKey<T>,
        @JvmField
        protected val constructor: (properties: Item.Properties) -> T,
        @JvmField
        protected val parent: ItemContentFactory
    ) : ObjectBuilder<RegisteredObject<T>> {
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

        fun properties(properties: UnaryOperator<Item.Properties>): B {
            this.properties = properties.apply(this.properties)
            return this as B
        }

        fun client(info: UnaryOperator<ClientInfo<*>>): B {
            if (PlatformUtil.INSTANCE.isClient()) {
                clientInfo = info.apply(clientInfo ?: ClientInfoImpl())
            }

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

            if (!parent.toRegister.add(item)) {
                throw IllegalArgumentException("Cannot create two items under the same ID $key")
            }

            for (tag in tags) {
                parent.dynamicTags.computeIfAbsent(tag) { hashSetOf() }.add(key)
            }

            return item
        }
    }
}
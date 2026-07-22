package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.typho.big_shot_lib.api.NeoCommonInitializer

@Suppress("UNCHECKED_CAST")
open class RecipeTypeFactory(
    mod: NeoCommonInitializer
) : ContentFactory<RecipeSerializer<*>> {
    override val registry: ResourceKey<Registry<RecipeSerializer<*>>> = Registries.RECIPE_SERIALIZER
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashSetOf<RegisteredObject.Immediate<out RecipeSerializer<*>>>()

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

    open fun <T : Recipe<*>> create(key: Identifier, serializer: RecipeSerializer<T>): RegisteredObject<RecipeSerializer<T>> {
        if (registered) {
            throw IllegalStateException("ContentFactory $this has ended, it cannot receive more entries")
        }

        val obj = RegisteredObject.Immediate(ResourceKey.create(registry, key) as ResourceKey<RecipeSerializer<T>>, serializer)

        if (!toRegister.add(obj)) {
            throw IllegalArgumentException("Cannot create two recipe types under the same ID $key")
        }

        return obj
    }
}
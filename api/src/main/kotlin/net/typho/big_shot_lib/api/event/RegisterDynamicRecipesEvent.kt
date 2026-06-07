package net.typho.big_shot_lib.api.event

import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import java.util.function.Consumer

fun interface RegisterDynamicRecipesEvent {
    fun register(output: Output, registries: HolderLookup.Provider)

    interface Output {
        fun register(out: Consumer<RecipeOutput>)

        fun register(location: ResourceKey<out Recipe<*>>, recipe: RecipeBuilder) {
            register { recipe.save(it, location.location()) }
        }

        fun register(location: ResourceKey<out Recipe<*>>, recipe: Recipe<*>)
    }
}
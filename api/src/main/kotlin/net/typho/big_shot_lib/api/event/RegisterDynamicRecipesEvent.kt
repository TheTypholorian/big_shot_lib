package net.typho.big_shot_lib.api.event

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

//? neoforge {
import net.neoforged.neoforge.common.conditions.ICondition
//? }

fun interface RegisterDynamicRecipesEvent {
    fun register(output: Output, registries: HolderLookup.Provider)

    interface Output {
        fun register(location: Identifier, recipe: RecipeBuilder) {
            recipe.save(object : RecipeOutput {
                //? fabric {
                /*override fun accept(location: Identifier, recipe: Recipe<*>, advancement: AdvancementHolder?) {
                    register(location, recipe)
                }
                *///? } neoforge {
                override fun accept(location: Identifier, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) {
                    register(location, recipe) // TODO implement this conditions system
                }
                //? }

                override fun advancement(): Advancement.Builder {
                    return Advancement.Builder.recipeAdvancement() // TODO advancements
                }
            }, location)
        }

        fun register(location: Identifier, recipe: Recipe<*>)
    }
}
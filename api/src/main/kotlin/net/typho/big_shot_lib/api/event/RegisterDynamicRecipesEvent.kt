package net.typho.big_shot_lib.api.event

import com.google.common.collect.ImmutableMap
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe

//? neoforge {
import net.neoforged.neoforge.common.conditions.ICondition
//? }

fun interface RegisterDynamicRecipesEvent {
    fun register(output: Output, registries: HolderLookup.Provider)

    interface Output {
        fun register(location: ResourceKey<out Recipe<*>>, recipe: RecipeBuilder) {
            recipe.save(object : RecipeOutput {
                //? fabric {
                /*override fun accept(location: Identifier, recipe: Recipe<*>, advancement: AdvancementHolder?) {
                    register(ResourceKey.create(Registries.RECIPE, location), recipe)
                }
                *///? } neoforge {
                override fun accept(location: Identifier, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) {
                    register(ResourceKey.create(Registries.RECIPE, location), recipe) // TODO implement this conditions system
                }
                //? }

                override fun advancement(): Advancement.Builder {
                    return Advancement.Builder.recipeAdvancement() // TODO advancements
                }
            }, location.location())
        }

        fun register(location: ResourceKey<out Recipe<*>>, recipe: Recipe<*>)

        class Advancements(
            @JvmField
            val builder: ImmutableMap.Builder<Identifier, AdvancementHolder>
        ) : Output {
            @JvmField
            var count = 0

            override fun register(location: ResourceKey<out Recipe<*>>, recipe: RecipeBuilder) {
                recipe.save(object : RecipeOutput {
                    override fun advancement(): Advancement.Builder {
                        return Advancement.Builder.recipeAdvancement()
                    }

                    //? fabric {
                    /*override fun accept(
                        identifier: Identifier,
                        recipe: Recipe<*>,
                        advancement: AdvancementHolder?
                    ) {
                        if (advancement != null) {
                            builder.put(advancement.id(), advancement)
                        }
                    }
                    *///? } neoforge {
                    override fun accept(
                        identifier: Identifier,
                        recipe: Recipe<*>,
                        advancement: AdvancementHolder?,
                        vararg iConditions: ICondition
                    ) {
                        if (advancement != null) {
                            builder.put(advancement.id(), advancement)
                            count++
                        }
                    }
                    //? }
                }, location.location())
            }

            override fun register(location: ResourceKey<out Recipe<*>>, recipe: Recipe<*>) {
            }
        }
    }
}
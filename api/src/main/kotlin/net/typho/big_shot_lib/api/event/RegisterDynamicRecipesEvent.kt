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
import java.util.function.Consumer

//? neoforge {
import net.neoforged.neoforge.common.conditions.ICondition
//? }

// TODO why stonecutter in api????
fun interface RegisterDynamicRecipesEvent {
    fun register(output: Output, registries: HolderLookup.Provider)

    interface Output {
        fun register(out: Consumer<RecipeOutput>) {
            out.accept(object : RecipeOutput {
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
            })
        }

        fun register(location: ResourceKey<out Recipe<*>>, recipe: RecipeBuilder) {
            register { recipe.save(it, location.location()) }
        }

        fun register(location: ResourceKey<out Recipe<*>>, recipe: Recipe<*>)

        class Advancements(
            @JvmField
            val builder: ImmutableMap.Builder<Identifier, AdvancementHolder>
        ) : Output {
            @JvmField
            var count = 0

            override fun register(out: Consumer<RecipeOutput>) {
                out.accept(object : RecipeOutput {
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
                })
            }

            override fun register(location: ResourceKey<out Recipe<*>>, recipe: Recipe<*>) {
            }
        }
    }
}
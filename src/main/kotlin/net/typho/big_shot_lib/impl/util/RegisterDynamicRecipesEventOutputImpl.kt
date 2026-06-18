package net.typho.big_shot_lib.impl.util

//? neoforge {

//? }

/*
interface RegisterDynamicRecipesEventOutputImpl : RegisterDynamicRecipesEvent.Output {
    override fun register(out: Consumer<RecipeOutput>) {
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

    class Advancements(
        @JvmField
        val builder: ImmutableMap.Builder<Identifier, AdvancementHolder>
    ) : RegisterDynamicRecipesEventOutputImpl {
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
 */
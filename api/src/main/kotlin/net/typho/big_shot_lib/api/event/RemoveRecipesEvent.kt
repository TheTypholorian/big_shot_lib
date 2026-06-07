package net.typho.big_shot_lib.api.event

import net.minecraft.world.item.crafting.RecipeHolder

fun interface RemoveRecipesEvent {
    fun shouldRemove(recipe: RecipeHolder<*>): Boolean
}
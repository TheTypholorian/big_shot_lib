package net.typho.eye_spy

import com.mojang.serialization.MapCodec
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

object SpyglassRecipe : CraftingRecipe, RecipeSerializer<SpyglassRecipe> {
    override fun category(): CraftingBookCategory {
        return CraftingBookCategory.EQUIPMENT
    }

    override fun matches(
        input: CraftingInput,
        level: Level
    ): Boolean {
        for (x in 0 until input.width()) {
            for (y in 0 until input.height() - 2) {
                if (!input.getItem(x, y).`is`(Items.AMETHYST_SHARD)) {
                    continue
                }

                val trim = input.getItem(x, y + 1).item

                if (SpyglassTrimMaterial.REGISTRY.values.none { it.item.get() == trim }) {
                    continue
                }

                val base = input.getItem(x, y + 2).item

                if (SpyglassBaseMaterial.REGISTRY.values.none { it.item.get() == base }) {
                    continue
                }

                return true
            }
        }

        return false
    }

    override fun assemble(
        input: CraftingInput,
        lookup: HolderLookup.Provider
    ): ItemStack {
        for (x in 0 until input.width()) {
            for (y in 0 until input.height() - 2) {
                if (!input.getItem(x, y).`is`(Items.AMETHYST_SHARD)) {
                    continue
                }

                val trim = input.getItem(x, y + 1).item
                val trimMaterial = SpyglassTrimMaterial.REGISTRY.values.firstOrNull { it.item.get() == trim } ?: continue

                val base = input.getItem(x, y + 2).item
                val baseMaterial = SpyglassBaseMaterial.REGISTRY.values.firstOrNull { it.item.get() == base } ?: continue

                val data = SpyglassData(
                    ItemStack(EyeSpy.basicLens.get()),
                    listOf(),
                    baseMaterial,
                    trimMaterial
                )
                val stack = ItemStack(Items.SPYGLASS)
                stack.set(EyeSpy.spyglassDataComponent.get(), data)
                return stack
            }
        }

        return ItemStack.EMPTY
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width >= 1 && height >= 3
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return ItemStack(Items.SPYGLASS)
    }

    override fun codec(): MapCodec<SpyglassRecipe> = MapCodec.unit(SpyglassRecipe)

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SpyglassRecipe> = StreamCodec.unit(SpyglassRecipe)

    override fun getSerializer(): RecipeSerializer<*> {
        return this
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(
            Ingredient.EMPTY,
            Ingredient.of(Items.AMETHYST_SHARD),
            Ingredient.of(SpyglassTrimMaterial.REGISTRY.values.stream().map { ItemStack(it.item.get()) }),
            Ingredient.of(SpyglassBaseMaterial.REGISTRY.values.stream().map { ItemStack(it.item.get()) })
        )
    }
}
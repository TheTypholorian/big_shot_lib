package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.typho.eye_spy.SpyglassRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(PlaceRecipe.class)
public interface PlaceRecipeMixin<T> {
    @Inject(
            method = "placeRecipe",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/crafting/RecipeHolder;value()Lnet/minecraft/world/item/crafting/Recipe;"
            )
    )
    private void placeRecipe(
            int width,
            int height,
            int outputSlot,
            RecipeHolder<?> recipe,
            Iterator<T> ingredients,
            int maxAmount,
            CallbackInfo ci,
            @Local(ordinal = 4) LocalIntRef widthRef,
            @Local(ordinal = 5) LocalIntRef heightRef
    ) {
        if (recipe.value() instanceof SpyglassRecipe) {
            widthRef.set(1);
            heightRef.set(3);
        }
    }
}

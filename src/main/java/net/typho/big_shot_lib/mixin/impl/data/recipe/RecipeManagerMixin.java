package net.typho.big_shot_lib.mixin.impl.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.typho.big_shot_lib.api.BigShotLib;
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent;
import net.typho.big_shot_lib.impl.NeoEventBusImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;entrySet()Ljava/util/Set;"
            )
    )
    private void apply(
            Map<Identifier, JsonElement> object,
            ResourceManager resourceManager,
            ProfilerFiller profiler,
            CallbackInfo ci,
            @Local ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byTypeBuilder,
            @Local ImmutableMap.Builder<Identifier, RecipeHolder<?>> byNameBuilder
    ) {
        int[] counter = { 0 };

        for (RegisterDynamicRecipesEvent event : NeoEventBusImpl.DYNAMIC_RECIPE_EVENTS) {
            event.register((location, recipe) -> {
                RecipeHolder<?> holder = new RecipeHolder<>(location.location(), recipe);
                byTypeBuilder.put(recipe.getType(), holder);
                byNameBuilder.put(location.location(), holder);
                counter[0]++;
            }, registries);
        }

        BigShotLib.LOGGER.info("Loaded {} dynamic recipes", counter[0]);
    }
}

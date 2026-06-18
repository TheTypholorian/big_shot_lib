package net.typho.big_shot_lib.mixin.impl.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.typho.big_shot_lib.api.BigShotLib;
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent;
import net.typho.big_shot_lib.api.event.RemoveRecipesEvent;
import net.typho.big_shot_lib.impl.NeoEventBusImpl;
import net.typho.big_shot_lib.impl.util.RegisterDynamicRecipesEventOutputImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Inject(
            method = "lambda$apply$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;"
            ),
            cancellable = true
    )
    private static void apply(
            Identifier id,
            ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder,
            ImmutableMap.Builder<Identifier, RecipeHolder<?>> builder1,
            WithConditions<Recipe<?>> conditions,
            CallbackInfo ci,
            @Local RecipeHolder<?> recipe
    ) {
        for (RemoveRecipesEvent event : NeoEventBusImpl.REMOVE_RECIPE_EVENTS) {
            if (event.shouldRemove(recipe)) {
                ci.cancel();
                break;
            }
        }
    }

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
            event.register((RegisterDynamicRecipesEventOutputImpl) (location, recipe) -> {
                RecipeHolder<?> holder = new RecipeHolder<>(location.identifier(), recipe);
                byTypeBuilder.put(recipe.getType(), holder);
                byNameBuilder.put(location.identifier(), holder);
                counter[0]++;
            }, registries);
        }

        BigShotLib.LOGGER.info("Loaded {} dynamic recipes", counter[0]);
    }
}

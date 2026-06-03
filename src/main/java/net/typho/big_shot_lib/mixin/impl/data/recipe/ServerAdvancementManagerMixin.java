package net.typho.big_shot_lib.mixin.impl.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent;
import net.typho.big_shot_lib.impl.NeoEventBusImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"
            )
    )
    private void apply(
            Map<Identifier, JsonElement> object,
            ResourceManager resourceManager,
            ProfilerFiller profiler,
            CallbackInfo ci,
            @Local ImmutableMap.Builder<Identifier, AdvancementHolder> builder
    ) {
        int[] counter = { 0 };

        for (RegisterDynamicRecipesEvent event : NeoEventBusImpl.DYNAMIC_RECIPE_EVENTS) {
            event.register(new RegisterDynamicRecipesEvent.Output.Advancements(builder), registries);
        }

        BigShotApi.LOGGER.info("Loaded {} dynamic recipe advancements", counter[0]);
    }
}

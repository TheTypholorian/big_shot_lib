package net.typho.big_shot_lib.mixin.impl.client.assets.model;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.event.BlockModelLoadingEvent;
import net.typho.big_shot_lib.impl.client.NeoClientEventBusImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Inject(
            method = "lambda$loadBlockModels$10",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;entrySet()Ljava/util/Set;"
            )
    )
    private static void loadBlockModels(
            Executor executor,
            Map<Identifier, Resource> resources,
            CallbackInfoReturnable<CompletionStage<Map<Identifier, BlockModel>>> cir,
            @Local List<CompletableFuture<Pair<Identifier, BlockModel>>> models
    ) {
        for (BlockModelLoadingEvent event : NeoClientEventBusImpl.BLOCK_MODEL_LOADING_EVENTS) {
            event.loadModels((location, model) -> {
                models.add(CompletableFuture.supplyAsync(() -> new Pair<>(location.withPrefix("models/").withSuffix(".json"), model.invoke()), executor));
            });
        }
    }

    @Inject(
            method = "lambda$loadBlockStates$14",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;entrySet()Ljava/util/Set;"
            )
    )
    private static void loadBlockStates(
            Executor executor,
            Map<Identifier, List<Resource>> resources,
            CallbackInfoReturnable<CompletionStage<Map<Identifier, List<BlockStateModelLoader.LoadedJson>>>> cir,
            @Local List<CompletableFuture<Pair<Identifier, List<BlockStateModelLoader.LoadedJson>>>> states
    ) {
        for (BlockModelLoadingEvent event : NeoClientEventBusImpl.BLOCK_MODEL_LOADING_EVENTS) {
            event.loadStates((location, state) -> {
                states.add(CompletableFuture.supplyAsync(() -> new Pair<>(location.withPrefix("blockstates/").withSuffix(".json"), Collections.singletonList(new BlockStateModelLoader.LoadedJson(BigShotApi.id("dynamic_block_models").toString(), state.invoke()))), executor));
            });
        }
    }
}

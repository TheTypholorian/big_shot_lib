package net.typho.big_shot_lib.mixin.impl.client.assets.model;

import com.google.gson.JsonElement;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.event.BlockModelLoadingEvent;
import net.typho.big_shot_lib.impl.client.NeoClientEventBusImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @ModifyArg(
            method = "reload",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;thenCombineAsync(Ljava/util/concurrent/CompletionStage;Ljava/util/function/BiFunction;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            ),
            index = 1
    )
    private static BiFunction<Map<Identifier, BlockModel>, Map<Identifier, List<BlockStateModelLoader.LoadedJson>>, ModelBakery> reload(
            BiFunction<Map<Identifier, BlockModel>, Map<Identifier, List<BlockStateModelLoader.LoadedJson>>, ModelBakery> func
    ) {
        return (models, states) -> {
            var mutableModels = MapsKt.toMutableMap(models);
            var mutableStates = MapsKt.toMutableMap(states);

            int[] numModels = { 0 };
            int[] numStates = { 0 };

            var output = new BlockModelLoadingEvent.Output() {
                @Override
                public void registerStateJson(@NotNull Identifier location, @NotNull JsonElement state) {
                    mutableStates.put(location.withPrefix("blockstates/").withSuffix(".json"), Collections.singletonList(new BlockStateModelLoader.LoadedJson(BigShotApi.id("dynamic_block_models").toString(), state)));
                    numModels[0]++;
                }

                @Override
                public void register(@NotNull Identifier location, @NotNull BlockModel model) {
                    mutableModels.put(location.withPrefix("models/").withSuffix(".json"), model);
                    numStates[0]++;
                }
            };

            for (BlockModelLoadingEvent event : NeoClientEventBusImpl.BLOCK_MODEL_LOADING_EVENTS) {
                event.load(output);
            }

            BigShotApi.LOGGER.info("Loaded {} dynamic models, and {} block states", numModels[0], numStates[0]);

            return func.apply(mutableModels, mutableStates);
        };
    }
}

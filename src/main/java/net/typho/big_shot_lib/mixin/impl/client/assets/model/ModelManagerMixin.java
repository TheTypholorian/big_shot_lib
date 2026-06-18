package net.typho.big_shot_lib.mixin.impl.client.assets.model;

import kotlin.collections.MapsKt;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.BigShotLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.HashMap;
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
            var extraModels = new HashMap<Identifier, BlockModel>();
            var extraStates = new HashMap<Identifier, List<BlockStateModelLoader.LoadedJson>>();

            int[] numStates = { 0 };
            int[] numModels = { 0 };

            /*
            var output = new ModelLoadingEvent.Output() {
                @Override
                public void registerStateJson(@NotNull Identifier location, @NotNull JsonElement state) {
                    var old = extraStates.putIfAbsent(location.withPrefix("blockstates/").withSuffix(".json"), Collections.singletonList(new BlockStateModelLoader.LoadedJson(BigShotLib.id("dynamic_block_models").toString(), state)));

                    if (old == null) {
                        numStates[0]++;
                    }
                }

                @Override
                public void register(@NotNull Identifier location, @NotNull BlockModel model) {
                    var old = extraModels.putIfAbsent(location, model);
                    BigShotLib.LOGGER.info("Registering model {}, old: {}", location, old);

                    if (old == null) {
                        numModels[0]++;
                    }
                }
            };

            for (ModelLoadingEvent event : NeoClientEventBusImpl.MODEL_LOADING_EVENTS) {
                event.load(output);
            }
             */

            BigShotLib.LOGGER.info("Loaded {} dynamic models, and {} dynamic block states", numModels[0], numStates[0]);

            var mutableModels = MapsKt.toMutableMap(models);
            var mutableStates = MapsKt.toMutableMap(states);

            extraModels.forEach((id, model) -> mutableModels.putIfAbsent(id.withPrefix("models/").withSuffix(".json"), model));
            extraStates.forEach(mutableStates::putIfAbsent);

            var bakery = func.apply(mutableModels, mutableStates);
            var accessor = (ModelBakeryAccessor) bakery;

            extraModels.forEach((id, model) -> {
                if (id.getPath().startsWith("item/")) {
                    BigShotLib.LOGGER.info("Loading item {} {}", id, model);
                    accessor.big_shot_lib$registerModelAndLoadDependencies(ModelIdentifier.inventory(id.withPath(path -> path.substring("item/".length()))), model);
                }
            });

            for (BlockModel model : extraModels.values()) {
                model.resolveParents(accessor::big_shot_lib$getModel);
            }

            return bakery;
        };
    }
}

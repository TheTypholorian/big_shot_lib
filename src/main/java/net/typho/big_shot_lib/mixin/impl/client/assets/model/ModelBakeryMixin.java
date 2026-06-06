package net.typho.big_shot_lib.mixin.impl.client.assets.model;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.ProfilerFiller;
import net.typho.big_shot_lib.api.BigShotLib;
import net.typho.big_shot_lib.impl.client.NeoClientEventBusImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @Shadow
    protected abstract void registerModelAndLoadDependencies(ModelIdentifier modelLocation, UnbakedModel model);

    /*
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/DefaultedRegistry;keySet()Ljava/util/Set;"
            )
    )
    private void reload(
            BlockColors blockColors,
            ProfilerFiller profilerFiller,
            Map<Identifier, BlockModel> modelResources,
            Map<Identifier, List<BlockStateModelLoader.LoadedJson>> blockStateResources,
            CallbackInfo ci
    ) {
        int[] numModels = { 0 };

        var output = new ItemModelLoadingEvent.Output() {
            @Override
            public void register(@NotNull ModelIdentifier location, @NotNull UnbakedModel model) {
                registerModelAndLoadDependencies(location, model);
                numModels[0]++;
            }
        };

        for (ItemModelLoadingEvent event : NeoClientEventBusImpl.ITEM_MODEL_LOADING_EVENTS) {
            event.load(output);
        }

        BigShotLib.LOGGER.info("Loaded {} dynamic item models", numModels[0]);
    }
     */
}

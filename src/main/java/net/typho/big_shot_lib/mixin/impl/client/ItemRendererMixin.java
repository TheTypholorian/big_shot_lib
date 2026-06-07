package net.typho.big_shot_lib.mixin.impl.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.typho.eye_spy.EyeSpy;
import net.typho.eye_spy.EyeSpyClient;
import net.typho.eye_spy.SpyglassData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/ModelManager;getModel(Lnet/minecraft/client/resources/model/ModelIdentifier;)Lnet/minecraft/client/resources/model/BakedModel;",
                    ordinal = 1
            )
    )
    private BakedModel render(
            ModelManager instance,
            ModelIdentifier modelLocation,
            Operation<BakedModel> original,
            @Local(argsOnly = true) ItemStack stack
    ) {
        SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

        if (data != null) {
            return EyeSpyClient.getSpyglassModel(data);
            //return ModelIdentifier.inventory(BuiltInRegistries.ITEM.getKey(data.lens.getItem()).withSuffix("_in_spyglass"));
        }

        return original.call(instance, modelLocation);
    }

    @ModifyArg(
            method = "getModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/ModelManager;getModel(Lnet/minecraft/client/resources/model/ModelIdentifier;)Lnet/minecraft/client/resources/model/BakedModel;",
                    ordinal = 1
            )
    )
    private ModelIdentifier getModel(
            ModelIdentifier model,
            @Local(argsOnly = true) ItemStack stack
    ) {
        SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

        if (data != null) {
            if (data.lens.isEmpty()) {
                return ModelIdentifier.inventory(EyeSpy.id("empty_spyglass_in_hand"));
            }

            return ModelIdentifier.inventory(BuiltInRegistries.ITEM.getKey(data.lens.getItem()).withSuffix("_in_spyglass_in_hand"));
        }

        return model;
    }
}

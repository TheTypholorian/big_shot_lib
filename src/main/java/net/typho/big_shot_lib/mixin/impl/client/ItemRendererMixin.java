package net.typho.big_shot_lib.mixin.impl.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.typho.eye_spy.EyeSpy;
import net.typho.eye_spy.SpyglassData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/ModelManager;getModel(Lnet/minecraft/client/resources/model/ModelIdentifier;)Lnet/minecraft/client/resources/model/BakedModel;",
                    ordinal = 1
            )
    )
    private ModelIdentifier render(
            ModelIdentifier model,
            @Local(argsOnly = true) ItemStack stack
    ) {
        SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

        if (data != null) {
            if (data.lens.isEmpty()) {
                return ModelIdentifier.inventory(EyeSpy.id("empty_spyglass"));
            }

            if (data.lens.getItem() != EyeSpy.basicLens.get()) {
                return ModelIdentifier.inventory(BuiltInRegistries.ITEM.getKey(data.lens.getItem()).withSuffix("_in_spyglass"));
            }
        }

        return model;
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

            if (data.lens.getItem() != EyeSpy.basicLens.get()) {
                return ModelIdentifier.inventory(BuiltInRegistries.ITEM.getKey(data.lens.getItem()).withSuffix("_in_spyglass_in_hand"));
            }
        }

        return model;
    }
}

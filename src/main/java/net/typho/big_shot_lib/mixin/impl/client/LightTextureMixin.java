package net.typho.big_shot_lib.mixin.impl.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.typho.eye_spy.EyeSpy;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "updateLightTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;lerp(Lorg/joml/Vector3fc;F)Lorg/joml/Vector3f;",
                    ordinal = 0
            )
    )
    private void updateLightTexture(
            float partialTicks,
            CallbackInfo ci,
            @Local(ordinal = 7) LocalFloatRef brightness
    ) {
        if (minecraft.cameraEntity instanceof Player player && !minecraft.gameRenderer.getMainCamera().isDetached()) {
            var data = player.getUseItem().get(EyeSpy.spyglassDataComponent.get());

            if (data != null && data.lens.is(EyeSpy.nightVisionLens.get())) {
                brightness.set(1f);
            }
        }
    }

    @Inject(
            method = "updateLightTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;mul(F)Lorg/joml/Vector3f;",
                    ordinal = 2
            )
    )
    private void updateLightTexture(
            float partialTicks,
            CallbackInfo ci,
            @Local(ordinal = 1) Vector3f color,
            @Local(ordinal = 0) int skyLight,
            @Local(ordinal = 1) int blockLight
    ) {
        if (minecraft.cameraEntity instanceof Player player && !minecraft.gameRenderer.getMainCamera().isDetached()) {
            var data = player.getUseItem().get(EyeSpy.spyglassDataComponent.get());

            if (data != null) {
                if (data.lens.is(EyeSpy.nightVisionLens.get())) {
                    float light = 1 - Math.max(skyLight, blockLight) / 15f;
                    color.lerp(new Vector3f(124 / 255f, 178 / 255f, 71 / 255f), light * light);
                }
            }
        }
    }
}

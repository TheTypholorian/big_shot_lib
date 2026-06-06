package net.typho.big_shot_lib.mixin.impl.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
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
        if (minecraft.cameraEntity instanceof Player player) {
            ItemStack item = player.getUseItem();

            if (item.getItem() instanceof SpyglassItem) {
                brightness.set(1f);
            }
        }
    }
}

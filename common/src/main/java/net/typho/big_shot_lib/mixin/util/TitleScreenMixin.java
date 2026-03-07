package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(
            method = "registerTextures",
            at = @At("TAIL")
    )
    private static void preloadResources(
            TextureManager textureManager,
            CallbackInfo ci
    ) {
        for (CubeMap map : BigShotClientEventStorage.panoramaCubeMaps.values()) {
            map.registerTextures(textureManager);
        }
    }
}

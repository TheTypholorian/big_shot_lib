package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @SuppressWarnings("unchecked")
    @ModifyReturnValue(
            method = "preloadResources",
            at = @At("RETURN")
    )
    private static CompletableFuture<Void> preloadResources(
            CompletableFuture<Void> original,
            @Local(argsOnly = true) TextureManager manager,
            @Local(argsOnly = true) Executor executor
    ) {
        CompletableFuture<Void>[] array = new CompletableFuture[BigShotClientEventStorage.panoramaCubeMaps.size() + 1];
        array[0] = original;

        int i = 1;

        for (CubeMap map : BigShotClientEventStorage.panoramaCubeMaps.values()) {
            array[i++] = map.preload(manager, executor);
        }

        return CompletableFuture.allOf(array);
    }
}

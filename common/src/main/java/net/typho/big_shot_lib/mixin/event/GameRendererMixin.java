package net.typho.big_shot_lib.mixin.event;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "renderLevel",
            at = @At("HEAD")
    )
    private void renderLevelHead(DeltaTracker deltaTracker, CallbackInfo ci) {
        BigShotClientEventStorage.onFrameStart.forEach(Runnable::run);
    }

    @Inject(
            method = "renderLevel",
            at = @At("TAIL")
    )
    private void renderLevelTail(DeltaTracker deltaTracker, CallbackInfo ci) {
        BigShotClientEventStorage.onFrameEnd.forEach(Runnable::run);
    }
}

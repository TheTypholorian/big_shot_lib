package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.typho.big_shot_lib.BigShotLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(
            method = "flipFrame",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/Tesselator;clear()V"
            )
    )
    private static void flipFrame(Window window, TracyFrameCapture frameCapture, CallbackInfo ci) {
        while (!BigShotLib.renderThreadQueue.isEmpty()) {
            BigShotLib.renderThreadQueue.poll().run();
        }
    }
}

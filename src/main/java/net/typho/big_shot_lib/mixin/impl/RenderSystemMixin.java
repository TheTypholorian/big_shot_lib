package net.typho.big_shot_lib.mixin.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.typho.big_shot_lib.impl.client.rendering.opengl.GlQueueImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(
            method = "flipFrame",
            at = @At("TAIL")
    )
    private static void flipFrame(CallbackInfo ci) {
        synchronized (GlQueueImpl.queue) {
            for (Runnable task : GlQueueImpl.queue) {
                task.run();
            }

            GlQueueImpl.queue.clear();
        }
    }
}

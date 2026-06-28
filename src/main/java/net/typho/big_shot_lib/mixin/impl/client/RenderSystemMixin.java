package net.typho.big_shot_lib.mixin.impl.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.typho.big_shot_lib.impl.client.rendering.opengl.GpuQueueImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(
            method = "pollEvents",
            at = @At("TAIL")
    )
    private static void pollEvents(CallbackInfo ci) {
        synchronized (GpuQueueImpl.queue) {
            for (Runnable task : GpuQueueImpl.queue) {
                task.run();
            }

            GpuQueueImpl.queue.clear();
        }
    }
}

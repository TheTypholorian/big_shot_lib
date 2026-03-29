package net.typho.big_shot_lib.impl.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.typho.big_shot_lib.impl.client.rendering.opengl.GlQueueImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(
            method = "flipFrame",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;clear()V"
            )
    )
    private static void flipFrame(long l, CallbackInfo ci) {
        synchronized (GlQueueImpl.queue) {
            for (Function0<@NotNull Unit> task : GlQueueImpl.queue) {
                task.invoke();
            }

            GlQueueImpl.queue.clear();
        }
    }
}

package net.typho.big_shot_lib.mixin.util;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL;
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.DynamicBuffer;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import net.typho.big_shot_lib.impl.util.DynamicBufferRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin {
    @Inject(
            method = "createBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;checkStatus()V"
            )
    )
    private void createBuffers(
            int width,
            int height,
            CallbackInfo ci
    ) {
        if ((Object) this instanceof MainTarget) {
            List<Integer> buffers = new LinkedList<>();
            buffers.add(GL_COLOR_ATTACHMENT0);

            int i = GL_COLOR_ATTACHMENT1;

            for (DynamicBuffer<?> buffer : DynamicBufferRegistry.buffers) {
                int point = i++;
                buffers.add(point);
                buffer.resize(width, height, BufferUploader::uploadNull);
                buffer.attachToFramebuffer(point);
            }

            OpenGL.INSTANCE.drawBuffers(buffers.stream().mapToInt(j -> j).toArray());
        }
    }
}

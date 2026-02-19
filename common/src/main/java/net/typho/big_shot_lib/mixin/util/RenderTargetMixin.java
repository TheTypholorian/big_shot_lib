package net.typho.big_shot_lib.mixin.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBuffer;
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry;
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

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
            boolean clearError,
            CallbackInfo ci
    ) {
        List<Integer> buffers = new LinkedList<>();
        buffers.add(GL_COLOR_ATTACHMENT0);

        for (Map.Entry<Integer, DynamicBuffer> entry : DynamicBufferRegistry.buffers.entrySet()) {
            int point = GL_COLOR_ATTACHMENT0 + entry.getKey();
            buffers.add(point);
            entry.getValue().resize(width, height, BufferUploader::uploadNull);
            entry.getValue().attachToFramebuffer(point);
        }

        OpenGL.INSTANCE.drawBuffers(buffers.stream().mapToInt(j -> j).toArray());
    }
}

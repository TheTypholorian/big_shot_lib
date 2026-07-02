package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl.ssbo;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlRenderPass;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlResource;
import net.typho.big_shot_lib.impl.client.rendering.opengl.GlShaderStorageBufferStorage;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static org.lwjgl.opengl.GL30.glBindBufferRange;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

@Mixin(targets = "com/mojang/blaze3d/opengl/GlCommandEncoder")
public class GlCommandEncoderMixin {
    @Inject(
            method = "trySetup",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/opengl/GlProgram;getUniforms()Ljava/util/Map;",
                    ordinal = 1
            )
    )
    private void trySetup(
            GlRenderPass renderPass,
            Collection<String> uniforms,
            CallbackInfoReturnable<Boolean> cir
    ) {
        var storage = (GlShaderStorageBufferStorage) renderPass;

        for (Map.Entry<@NotNull String, @NotNull GpuBufferSlice> ssbo : storage.getBig_shot_lib$shaderStorageBuffers().entrySet()) {
            glBindBufferRange(GL_SHADER_STORAGE_BUFFER, storage.big_shot_lib$getShaderStorageBufferBinding(ssbo.getKey()), ((GlResource) ssbo.getValue().buffer()).getGlId(), ssbo.getValue().offset(), ssbo.getValue().length());
        }
    }
}

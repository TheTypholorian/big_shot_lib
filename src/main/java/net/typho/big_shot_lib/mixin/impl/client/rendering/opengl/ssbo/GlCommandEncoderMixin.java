package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl.ssbo;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlRenderPass;
import com.mojang.blaze3d.opengl.Uniform;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBufferUsage;
import net.typho.big_shot_lib.impl.client.rendering.common.StorageBufferUniformType;
import net.typho.big_shot_lib.impl.client.rendering.opengl.ssbo.UboUniformExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

@Mixin(targets = "com/mojang/blaze3d/opengl/GlCommandEncoder")
public class GlCommandEncoderMixin {
    @Inject(
            method = "trySetup",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;type()Lcom/mojang/blaze3d/shaders/UniformType;",
                    ordinal = 1
            )
    )
    private void trySetup(
            GlRenderPass renderPass,
            Collection<String> uniforms,
            CallbackInfoReturnable<Boolean> cir,
            @Local(ordinal = 0) BindGroupLayout.UniformDescription uniform,
            @Local(ordinal = 0) GpuBufferSlice value
    ) {
        if (uniform.type() == StorageBufferUniformType.INSTANCE) {
            if (value.buffer().isClosed()) {
                throw new IllegalStateException("Shader storage buffer " + uniform.name() + " is already closed");
            }

            if ((value.buffer().usage() & GpuBufferUsage.shaderStorage()) == 0) {
                throw new IllegalStateException("Shader storage buffer " + uniform.name() + " must have GpuBufferUsage.shaderStorage()");
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @ModifyConstant(
            method = "trySetup",
            constant = @Constant(intValue = GL_UNIFORM_BUFFER)
    )
    private int trySetup(
            int target,
            @Local Map.Entry<String, Uniform> uniform
    ) {
        return ((UboUniformExtension) (Object) uniform.getValue()).getBig_shot_lib$isSsbo() ? GL_SHADER_STORAGE_BUFFER : target;
    }

    @SuppressWarnings("DataFlowIssue")
    @ModifyConstant(
            method = "lambda$executeDrawMultiple$0",
            constant = @Constant(intValue = GL_UNIFORM_BUFFER)
    )
    private static int executeDrawMultiple(
            int target,
            @Local Uniform uniform
    ) {
        return ((UboUniformExtension) (Object) uniform).getBig_shot_lib$isSsbo() ? GL_SHADER_STORAGE_BUFFER : target;
    }
}

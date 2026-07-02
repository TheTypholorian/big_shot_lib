package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl.ssbo;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBufferImpl;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlRenderPipeline;
import net.typho.big_shot_lib.api.client.ext.RenderPassExtension;
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType;
import net.typho.big_shot_lib.api.util.Extension;
import net.typho.big_shot_lib.impl.client.rendering.opengl.GlShaderStorageBufferStorage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL43.*;

@Mixin(targets = "com/mojang/blaze3d/opengl/GlRenderPass")
public abstract class GlRenderPassMixin implements GlShaderStorageBufferStorage, RenderPassExtension {
    @Unique
    private final Map<String, GpuBufferSlice> big_shot_lib$shaderStorageBuffers = new HashMap<>();
    @Unique
    private final Map<String, Integer> big_shot_lib$shaderStorageBufferBindings = new HashMap<>();

    @Shadow
    public abstract void setUniform(String p_410717_, GpuBufferImpl p_418484_);
    @Shadow
    public abstract void setIndexBuffer(@Nullable GpuBufferImpl p_410828_, IndexType p_410040_);

    @Shadow
    @Nullable
    protected GlRenderPipeline pipeline;

    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
        setUniform(name, Extension.castTo(buffer));
    }

    @Override
    public void setStorageBuffer(@NotNull String name, @NotNull GpuBuffer buffer) {
        setStorageBuffer(name, buffer.slice());
    }

    @Override
    public void setStorageBuffer(@NotNull String name, @NotNull GpuBufferSlice buffer) {
        big_shot_lib$shaderStorageBuffers.put(name, buffer);
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
        setIndexBuffer(Extension.castTo(buffer), Extension.castTo(type));
    }

    @Override
    public @NonNull Map<String, GpuBufferSlice> getBig_shot_lib$shaderStorageBuffers() {
        return big_shot_lib$shaderStorageBuffers;
    }

    @Override
    public int big_shot_lib$getShaderStorageBufferBinding(@NotNull String name) {
        return big_shot_lib$shaderStorageBufferBindings.computeIfAbsent(name, key -> {
            int binding = big_shot_lib$shaderStorageBufferBindings.size();
            int program = Objects.requireNonNull(pipeline).program().getProgramId();
            int blockIndex = glGetProgramResourceIndex(
                    program,
                    GL_SHADER_STORAGE_BLOCK,
                    name
            );
            glShaderStorageBlockBinding(program, blockIndex, binding);
            return binding;
        });
    }
}

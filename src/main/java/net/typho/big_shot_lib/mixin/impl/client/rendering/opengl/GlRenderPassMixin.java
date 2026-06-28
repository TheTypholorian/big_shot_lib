package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBufferImpl;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.typho.big_shot_lib.api.client.ext.RenderPassExtension;
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType;
import net.typho.big_shot_lib.api.util.Extension;
import net.typho.big_shot_lib.impl.client.rendering.opengl.ShaderStorageBufferStorage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(targets = "com/mojang/blaze3d/opengl/GlRenderPass")
public abstract class GlRenderPassMixin implements ShaderStorageBufferStorage, RenderPassExtension {
    @Unique
    private final Map<Integer, GpuBufferSlice> big_shot_lib$shaderStorageBuffers = new HashMap<>();
    @Shadow
    @Final
    protected Set<String> dirtyUniforms;

    @Shadow
    public abstract void setUniform(String p_410717_, GpuBufferImpl p_418484_);
    @Shadow
    public abstract void setIndexBuffer(@Nullable GpuBufferImpl p_410828_, IndexType p_410040_);

    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
        setUniform(name, Extension.castTo(buffer));
    }

    @Override
    public void setStorageBuffer(int index, @NotNull GpuBuffer buffer) {
        setStorageBuffer(index, buffer.slice());
    }

    @Override
    public void setStorageBuffer(int index, @NotNull GpuBufferSlice buffer) {
        big_shot_lib$shaderStorageBuffers.put(index, buffer);
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
        setIndexBuffer(Extension.castTo(buffer), Extension.castTo(type));
    }

    @Override
    public @NonNull Map<Integer, GpuBufferSlice> getBig_shot_lib$shaderStorageBuffers() {
        return big_shot_lib$shaderStorageBuffers;
    }
}

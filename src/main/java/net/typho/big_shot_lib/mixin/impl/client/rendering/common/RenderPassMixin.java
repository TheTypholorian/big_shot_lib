package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderPassBackend;
import net.typho.big_shot_lib.api.client.ext.RenderPassExtension;
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPass.class)
public class RenderPassMixin implements RenderPassExtension {
    @Shadow
    @Final
    private RenderPassBackend backend;

    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
        backend.setUniform(name, buffer);
    }

    @Override
    public void setStorageBuffer(@NotNull String name, @NotNull GpuBuffer buffer) {
        backend.setStorageBuffer(name, buffer);
    }

    @Override
    public void setStorageBuffer(@NotNull String name, @NotNull GpuBufferSlice buffer) {
        backend.setStorageBuffer(name, buffer);
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
        backend.setIndexBuffer(buffer, type);
    }
}

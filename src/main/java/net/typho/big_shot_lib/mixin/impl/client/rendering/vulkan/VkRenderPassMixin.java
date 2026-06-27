package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.typho.big_shot_lib.api.client.ext.RenderPassExtension;
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com/mojang/blaze3d/opengl/VkRenderPass")
public class VkRenderPassMixin implements RenderPassExtension {
    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
    }

    @Override
    public void setStorageBuffer(@NotNull String name, @NotNull GpuBuffer buffer) {
    }

    @Override
    public void setStorageBuffer(@NotNull String name, @NotNull GpuBufferSlice buffer) {
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
    }
}

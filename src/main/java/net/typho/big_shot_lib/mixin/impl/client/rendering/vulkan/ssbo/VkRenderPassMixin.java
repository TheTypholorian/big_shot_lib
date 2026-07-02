package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBufferImpl;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.vulkan.VkBindGroupLayout;
import com.mojang.blaze3d.vulkan.VkBuffer;
import net.typho.big_shot_lib.api.client.ext.RenderPassExtension;
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType;
import net.typho.big_shot_lib.api.util.Extension;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.VkStorageBufferBindGroupEntryType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;

@Mixin(targets = "com/mojang/blaze3d/opengl/VkRenderPass")
public abstract class VkRenderPassMixin implements RenderPassExtension {
    @Unique
    private final Map<String, GpuBufferSlice> big_shot_lib$shaderStorageBuffers = new HashMap<>();

    @Shadow
    public abstract void setUniform(String p_410717_, GpuBufferImpl p_418484_);
    @Shadow
    public abstract void setIndexBuffer(@Nullable GpuBufferImpl p_410828_, IndexType p_410040_);

    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
        setUniform(name, Extension.castTo(buffer));
    }

    @Override
    public void setStorageBuffer(@NonNull String name, @NotNull GpuBuffer buffer) {
        setStorageBuffer(name, buffer.slice());
    }

    @Override
    public void setStorageBuffer(@NonNull String name, @NotNull GpuBufferSlice buffer) {
        big_shot_lib$shaderStorageBuffers.put(name, buffer);
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
        setIndexBuffer(Extension.castTo(buffer), Extension.castTo(type));
    }

    @Inject(
            method = "pushDescriptors",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vulkan/VkBindGroupLayout$Entry;type()Lcom/mojang/blaze3d/vulkan/VkBindGroupLayout$VulkanBindGroupEntryType;",
                    ordinal = 0
            )
    )
    private void pushDescriptors(CallbackInfo ci, @Local VkBindGroupLayout.Entry entry, @Local MemoryStack stack, @Local VkWriteDescriptorSet set) {
        if (entry.type() == VkStorageBufferBindGroupEntryType.INSTANCE) {
            GpuBufferSlice buffer = big_shot_lib$shaderStorageBuffers.get(entry.name());

            if (buffer != null) {
                VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo.calloc(1, stack);
                bufferInfo.buffer(((VkBuffer) buffer.buffer()).vkBuffer());
                bufferInfo.offset(buffer.offset());
                bufferInfo.range(buffer.length());
                set.descriptorType(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER);
                set.pBufferInfo(bufferInfo);
            }
        }
    }
}

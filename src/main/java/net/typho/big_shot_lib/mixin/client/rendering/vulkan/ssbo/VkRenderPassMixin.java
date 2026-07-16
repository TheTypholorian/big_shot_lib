package net.typho.big_shot_lib.mixin.client.rendering.vulkan.ssbo;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBufferImpl;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.vulkan.VkBindGroupLayout;
import com.mojang.blaze3d.vulkan.VkBuffer;
import net.typho.big_shot_lib.client.api.ext.RenderPassExtension;
import net.typho.big_shot_lib.client.api.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuBufferUsage;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuIndexType;
import net.typho.big_shot_lib.api.util.Extension;
import net.typho.big_shot_lib.client.impl.rendering.common.StorageBufferUniformType;
import net.typho.big_shot_lib.client.impl.rendering.vulkan.VkStorageBufferBindGroupEntryType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;

@Mixin(targets = "com/mojang/blaze3d/vulkan/VkRenderPass")
public abstract class VkRenderPassMixin implements RenderPassExtension {
    @Shadow
    public abstract void setUniform(String p_410717_, GpuBufferImpl p_418484_);
    @Shadow
    public abstract void setIndexBuffer(@Nullable GpuBufferImpl p_410828_, IndexType p_410040_);

    @Shadow
    @Final
    protected HashMap<String, GpuBufferSlice> uniforms;

    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
        setUniform(name, Extension.castTo(buffer));
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
        setIndexBuffer(Extension.castTo(buffer), Extension.castTo(type));
    }

    @Inject(
            method = "pushDescriptors",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;type()Lcom/mojang/blaze3d/shaders/UniformType;",
                    ordinal = 0
            )
    )
    private void pushDescriptors1(
            CallbackInfo ci,
            @Local BindGroupLayout.UniformDescription uniform,
            @Local GpuBufferSlice value
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
            GpuBufferSlice buffer = uniforms.get(entry.name());

            if (buffer == null) {
                throw new IllegalStateException("Missing uniform " + entry.name() + " (should be " + entry.type() + ")");
            } else {
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

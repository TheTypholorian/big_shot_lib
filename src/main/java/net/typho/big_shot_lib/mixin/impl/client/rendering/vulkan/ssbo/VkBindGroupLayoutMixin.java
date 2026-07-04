package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vulkan.VkBindGroupLayout;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.VkStorageBufferBindGroupEntryType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;

@Mixin(VkBindGroupLayout.class)
public class VkBindGroupLayoutMixin {
    @WrapOperation(
            method = "create",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vulkan/VkBindGroupLayout$Entry;type()Lcom/mojang/blaze3d/vulkan/VkBindGroupLayout$VulkanBindGroupEntryType;"
            )
    )
    private static VkBindGroupLayout.VulkanBindGroupEntryType create(
            VkBindGroupLayout.Entry instance,
            Operation<VkBindGroupLayout.VulkanBindGroupEntryType> original,
            @Local(argsOnly = true) List<VkBindGroupLayout.Entry> entries,
            @Local int i
    ) {
        if (entries.get(i).type() == VkStorageBufferBindGroupEntryType.INSTANCE) {
            return VkBindGroupLayout.VulkanBindGroupEntryType.UNIFORM_BUFFER;
        } else {
            return original.call(instance);
        }
    }

    @ModifyArg(
            method = "create",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/vulkan/VkDescriptorSetLayoutBinding;descriptorType(I)Lorg/lwjgl/vulkan/VkDescriptorSetLayoutBinding;"
            )
    )
    private static int create(
            int value,
            @Local(argsOnly = true) List<VkBindGroupLayout.Entry> entries,
            @Local int i
    ) {
        if (entries.get(i).type() == VkStorageBufferBindGroupEntryType.INSTANCE) {
            return VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;
        } else {
            return value;
        }
    }
}

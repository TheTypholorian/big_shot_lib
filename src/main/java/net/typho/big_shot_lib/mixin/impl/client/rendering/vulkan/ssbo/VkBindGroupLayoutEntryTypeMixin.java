package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.mojang.blaze3d.vulkan.VkBindGroupLayout;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VkBindGroupLayout.VulkanBindGroupEntryType.class)
public enum VkBindGroupLayoutEntryTypeMixin {
    BIG_SHOT_LIB_SHADER_STORAGE_BUFFER
}

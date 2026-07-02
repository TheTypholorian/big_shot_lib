package net.typho.big_shot_lib.impl.client.rendering.vulkan

import com.mojang.blaze3d.vulkan.VkBindGroupLayout

object VkStorageBufferBindGroupEntryType {
    @JvmField
    val INSTANCE = VkBindGroupLayout.VulkanBindGroupEntryType.valueOf("BIG_SHOT_LIB_SHADER_STORAGE_BUFFER")
}
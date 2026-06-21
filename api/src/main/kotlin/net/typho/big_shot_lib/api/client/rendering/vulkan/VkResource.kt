package net.typho.big_shot_lib.api.client.rendering.vulkan

import net.typho.big_shot_lib.api.client.rendering.common.GpuResource

interface VkResource: GpuResource, VkNamed {
    interface VMA : VkResource {
        val vmaPtr: Long
    }
}
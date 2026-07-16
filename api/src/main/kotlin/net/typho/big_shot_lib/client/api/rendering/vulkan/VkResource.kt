package net.typho.big_shot_lib.client.api.rendering.vulkan

import net.typho.big_shot_lib.client.api.rendering.common.GpuResource

interface VkResource: GpuResource, VkNamed {
    interface VMA : VkResource {
        val vmaPtr: Long
    }
}
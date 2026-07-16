package net.typho.big_shot_lib.client.impl.rendering.vulkan.ssbo

@JvmRecord
data class SpvStorageBuffer(
    @JvmField
    val name: String,
    @JvmField
    val bindingOffset: Int
)

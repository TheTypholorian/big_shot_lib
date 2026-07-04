package net.typho.big_shot_lib.impl.client.rendering.vulkan.ssbo

@JvmRecord
data class SpvStorageBuffer(
    @JvmField
    val name: String,
    @JvmField
    val bindingOffset: Int
)

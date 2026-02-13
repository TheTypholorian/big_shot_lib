package net.typho.big_shot_lib.api.client.rendering.shaders.variables

@JvmRecord
data class ShaderVariable(
    @JvmField
    val id: Int,
    @JvmField
    val name: String?,
    @JvmField
    val location: Int?,
    @JvmField
    val typePointer: Int,
    @JvmField
    val type: Int
)
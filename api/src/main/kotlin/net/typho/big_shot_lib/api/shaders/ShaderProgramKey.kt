package net.typho.big_shot_lib.api.shaders

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Named

@JvmRecord
data class ShaderProgramKey(
    @JvmField
    val loader: ShaderLoaderType,
    @JvmField
    val location: ResourceLocation,
    @JvmField
    val format: VertexFormat,
    @JvmField
    val sources: Set<ShaderSourceType>
) : Named {
    override fun location() = location
}

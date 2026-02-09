package net.typho.big_shot_lib.api.shaders

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.util.Named

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
    companion object {
        @JvmField
        val NULL = ShaderProgramKey(
            ShaderLoaderType.NULL,
            ResourceLocation.fromNamespaceAndPath("opengl", "null"),
            DefaultVertexFormat.POSITION,
            setOf()
        )
    }

    override fun location() = location

    override fun toString(): String {
        return location.toString()
    }
}

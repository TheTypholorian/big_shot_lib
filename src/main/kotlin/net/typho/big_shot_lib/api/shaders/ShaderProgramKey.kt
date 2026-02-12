package net.typho.big_shot_lib.api.shaders

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.Named

@JvmRecord
data class ShaderProgramKey(
    @JvmField
    val loader: ShaderLoaderType,
    @JvmField
    val location: ResourceIdentifier,
    @JvmField
    val format: VertexFormat,
    @JvmField
    val sources: Set<ShaderSourceType>
) : Named {
    companion object {
        @JvmField
        val NULL = ShaderProgramKey(
            ShaderLoaderType.NULL,
            ResourceIdentifier.fromNamespaceAndPath("opengl", "null"),
            DefaultVertexFormat.POSITION,
            setOf()
        )
    }

    override fun location() = location

    override fun toString(): String {
        return location.toString()
    }
}

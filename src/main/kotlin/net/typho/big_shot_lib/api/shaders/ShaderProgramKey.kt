package net.typho.big_shot_lib.api.shaders

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.util.Named
import net.typho.big_shot_lib.api.util.ResourceIdentifier

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
            ResourceIdentifier("opengl", "null"),
            DefaultVertexFormat.POSITION,
            setOf()
        )
    }

    override fun location() = location

    override fun toString(): String {
        return location.toString()
    }
}

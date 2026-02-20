package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.meshes.NeoVertexFormat
import net.typho.big_shot_lib.api.util.resources.Named
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class ShaderProgramKey(
    @JvmField
    val loader: ShaderLoaderType,
    @JvmField
    val location: ResourceIdentifier,
    @JvmField
    val format: NeoVertexFormat,
    @JvmField
    val sources: Set<ShaderSourceType>,
    @JvmField
    val builtinDynamicBuffers: Set<ResourceIdentifier>,
    @JvmField
    val disabledDynamicBuffers: Set<ResourceIdentifier>
) : Named {
    companion object {
        @JvmField
        val NULL = ShaderProgramKey(
            ShaderLoaderType.NULL,
            ResourceIdentifier("opengl", "null"),
            NeoVertexFormat.POSITION,
            setOf(),
            setOf(),
            setOf()
        )
    }

    override fun location() = location

    override fun toString(): String {
        return location.toString()
    }
}

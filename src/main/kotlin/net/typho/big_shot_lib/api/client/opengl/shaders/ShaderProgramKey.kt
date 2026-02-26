package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class ShaderProgramKey(
    @JvmField
    val loader: ShaderLoaderType,
    override val location: ResourceIdentifier,
    @JvmField
    val format: NeoVertexFormat,
    @JvmField
    val sources: Set<ShaderSourceType>,
    @JvmField
    val builtinDynamicBuffers: Set<ResourceIdentifier>,
    @JvmField
    val disabledDynamicBuffers: Set<ResourceIdentifier>
) : NamedResource {
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

    override fun toString(): String {
        return location.toString()
    }
}

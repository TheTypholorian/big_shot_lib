package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.shaders.variables.ShaderVariableType

open class NeoUniform(
    name: String,
    location: Int,
    type: ShaderVariableType,
    @JvmField
    val parent: NeoShader
) : GlUniform(name, location, type) {
    override fun pickSamplerUnit() = parent.pickSamplerUnit(location)

    override fun programKey() = parent.key
}
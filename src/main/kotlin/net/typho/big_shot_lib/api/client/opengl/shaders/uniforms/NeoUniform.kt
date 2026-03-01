package net.typho.big_shot_lib.api.client.opengl.shaders.uniforms

import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShader
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType

open class NeoUniform(
    name: String,
    location: Int,
    type: ShaderVariableType,
    @JvmField
    val parent: NeoShader
) : GlUniform(name, location, type) {
    override val programKey
        get() = parent.key

    override fun pickSamplerUnit() = parent.pickSamplerUnit(location)
}
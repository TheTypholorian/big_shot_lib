package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding

interface GlBoundProgram : GlBoundResource<GlProgram> {
    fun setUniform(name: String, value: GlUniform.() -> Unit)

    fun setTexture(unit: Int, binding: GlTextureBinding)

    fun setTextureArray(unit: Int, name: String, vararg bindings: GlTextureBinding)
}
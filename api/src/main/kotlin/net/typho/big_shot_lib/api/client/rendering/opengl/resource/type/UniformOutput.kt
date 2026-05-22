package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import java.util.function.Consumer

interface UniformOutput {
    fun setUniform(name: String, value: Consumer<GlUniform>)

    fun setTexture(index: Int, binding: GlTextureBinding)

    fun setTextureArray(index: Int, vararg bindings: GlTextureBinding)
}
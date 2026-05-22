package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.UniformOutput
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import java.util.function.Consumer

interface GlBoundProgram : GlBoundResource<GlProgram>, UniformOutput {
    override fun setUniform(
        name: String,
        value: Consumer<GlUniform>
    ) {
        resource.setUniform(name, value)
    }

    override fun setTexture(
        index: Int,
        binding: GlTextureBinding
    ) {
        resource.setTexture(index, binding)
    }

    override fun setTextureArray(
        index: Int,
        vararg bindings: GlTextureBinding
    ) {
        resource.setTextureArray(index, *bindings)
    }
}
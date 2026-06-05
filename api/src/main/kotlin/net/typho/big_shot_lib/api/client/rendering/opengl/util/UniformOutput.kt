package net.typho.big_shot_lib.api.client.rendering.opengl.util

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlUniform
import net.typho.big_shot_lib.api.client.rendering.state.TextureBinding
import java.util.function.Consumer

interface UniformOutput {
    fun setUniform(name: String, value: Consumer<GlUniform>)

    fun setTexture(index: Int, binding: TextureBinding)
}
package net.typho.big_shot_lib.api.client.rendering.opengl.util

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlUniform
import net.typho.big_shot_lib.api.client.rendering.state.TextureBinding
import net.typho.big_shot_lib.api.plugin.Namespace
import java.util.function.Consumer

@Namespace(BigShotApi.MOD_ID)
interface UniformOutput {
    fun setUniform(name: String, value: Consumer<GlUniform>)

    fun setTexture(index: Int, binding: TextureBinding)
}
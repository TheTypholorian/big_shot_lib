package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource

interface GlShaderShard : MaybeNamedResource, GlDrawStateShard {
    interface TextureBinding : MaybeNamedResource {
        val texture: GlTexture2D
        val target: GlTextureTarget
        val uniformName: String?
        val samplerId: Int?
    }

    val program: GlProgram
    val textures: Array<TextureBinding>

    fun setUniforms(program: GlBoundProgram)

    override fun bind(): GlBoundProgram {
        val program = program.use()

        textures.forEachIndexed { index, binding ->
            program.setTexture(binding.uniformName ?: "Sampler$index", index, binding.target, binding.texture, binding.samplerId ?: 0)
        }

        setUniforms(program)

        return program
    }
}
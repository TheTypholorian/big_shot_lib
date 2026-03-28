package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface GlShaderShard : MaybeNamedResource, GlDrawStateShard {
    interface TextureBinding : MaybeNamedResource {
        val texture: GlTexture2D
        val target: GlTextureTarget
        val sampler: GlSampler?
        val uniformName: String?
    }

    val program: GlProgram
    val textures: Array<out TextureBinding>

    fun setUniforms(program: GlBoundProgram)

    override fun bind(): GlBoundProgram {
        val program = program.use()

        textures.forEachIndexed { index, binding ->
            program.setTexture(binding.uniformName ?: "Sampler$index", index, binding.target, binding.texture, binding.sampler)
        }

        setUniforms(program)

        return program
    }

    companion object {
        @JvmStatic
        fun lookup(location: NeoIdentifier, uniforms: GlBoundProgram.() -> Unit, vararg textures: TextureBinding) = object : GlShaderShard {
            override val location: NeoIdentifier = location
            override val program: GlProgram
                get() = NeoShaderLoader[location]!!
            override val textures: Array<out TextureBinding> = textures

            override fun setUniforms(program: GlBoundProgram) {
                uniforms(program)
            }
        }

        @JvmStatic
        fun lookupTexture(location: NeoIdentifier, target: GlTextureTarget, sampler: GlSampler? = null, uniformName: String? = null) = object : TextureBinding {
            override val location: NeoIdentifier = location
            override val texture: GlTexture2D
                get() = GlTexture2D[location]
            override val target: GlTextureTarget = target
            override val sampler: GlSampler? = sampler
            override val uniformName: String? = uniformName
        }
    }
}
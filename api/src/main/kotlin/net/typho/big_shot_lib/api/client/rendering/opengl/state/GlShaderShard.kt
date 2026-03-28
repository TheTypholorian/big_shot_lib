package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.FileNotFoundException
import kotlin.collections.forEachIndexed

interface GlShaderShard : MaybeNamedResource, GlDrawStateShard {
    val program: GlProgram
    val uniforms: GlBoundProgram.() -> Unit
    val textures: List<GlTextureBinding>

    override fun bind(): GlBoundProgram {
        val program = program.use()

        textures.forEachIndexed { unit, binding ->
            program.setTexture(unit, binding)
        }

        uniforms(program)

        return program
    }

    data class FromLocation(
        override val location: NeoIdentifier,
        override val uniforms: GlBoundProgram.() -> Unit,
        override val textures: List<GlTextureBinding>
    ) : GlShaderShard {
        override val program: GlProgram
            get() = NeoShaderLoader[location] ?: throw FileNotFoundException("Couldn't find shader program $location")
    }

    data class FromInstance(
        override val program: GlProgram,
        override val uniforms: GlBoundProgram.() -> Unit,
        override val textures: List<GlTextureBinding>
    ) : GlShaderShard {
        override val location: NeoIdentifier = program.location
    }
}
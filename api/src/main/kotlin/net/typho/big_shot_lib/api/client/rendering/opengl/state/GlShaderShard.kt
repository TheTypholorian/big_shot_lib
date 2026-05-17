package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.FileNotFoundException
import java.util.function.Consumer

interface GlShaderShard : MaybeNamedResource, GlDrawStateShard {
    val program: GlProgram?
    val uniforms: Consumer<GlBoundProgram>
    val textures: Array<GlTextureBinding?>

    override fun bind(): GlBoundProgram {
        program?.let {
            val program = it.use()

            textures.forEachIndexed { index, binding ->
                if (binding != null) {
                    program.setTexture(index, binding)
                }
            }

            uniforms.accept(program)

            return program
        }

        return object : GlBoundProgram {
            override fun setUniform(
                name: String,
                value: Consumer<GlUniform>
            ) {
            }

            override fun setTexture(
                index: Int,
                binding: GlTextureBinding
            ) {
            }

            override fun setTextureArray(
                index: Int,
                vararg bindings: GlTextureBinding
            ) {
            }

            override val resource: GlProgram
                get() = throw NullPointerException()
            override val handle: GlStateStack.Handle<Int>
                get() = throw NullPointerException()
        }
    }

    fun texture(index: Int, binding: GlTextureBinding): GlShaderShard {
        textures[index] = binding
        return this
    }

    data class NoShader @JvmOverloads constructor(
        override val textures: Array<GlTextureBinding?> = Array(12) { null }
    ) : GlShaderShard {
        override val program: GlProgram
            get() = throw NullPointerException("No shader")
        override val uniforms: Consumer<GlBoundProgram> = Consumer { }
        override val location: NeoIdentifier? = null

        override fun texture(index: Int, binding: GlTextureBinding): GlShaderShard {
            textures[index] = binding
            return this
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NoShader) return false

            if (!textures.contentEquals(other.textures)) return false
            if (uniforms != other.uniforms) return false
            if (location != other.location) return false
            if (program != other.program) return false

            return true
        }

        override fun hashCode(): Int {
            var result = textures.contentHashCode()
            result = 31 * result + uniforms.hashCode()
            result = 31 * result + (location?.hashCode() ?: 0)
            result = 31 * result + program.hashCode()
            return result
        }
    }

    data class FromLocation @JvmOverloads constructor(
        override val location: NeoIdentifier,
        override val uniforms: Consumer<GlBoundProgram> = Consumer { },
        override val textures: Array<GlTextureBinding?> = Array(12) { null }
    ) : GlShaderShard {
        override val program: GlProgram
            // TODO
            get() = NeoShaderLoader[location] ?: throw FileNotFoundException("Couldn't find shader program $location")

        override fun texture(index: Int, binding: GlTextureBinding): FromLocation {
            textures[index] = binding
            return this
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FromLocation) return false

            if (location != other.location) return false
            if (uniforms != other.uniforms) return false
            if (!textures.contentEquals(other.textures)) return false
            if (program != other.program) return false

            return true
        }

        override fun hashCode(): Int {
            var result = location.hashCode()
            result = 31 * result + uniforms.hashCode()
            result = 31 * result + textures.contentHashCode()
            result = 31 * result + program.hashCode()
            return result
        }
    }

    data class FromInstance @JvmOverloads constructor(
        private val getter: () -> GlProgram?,
        override val uniforms: Consumer<GlBoundProgram> = Consumer { },
        override val textures: Array<GlTextureBinding?> = Array(12) { null }
    ) : GlShaderShard {
        override val program: GlProgram?
            get() = getter()
        override val location: NeoIdentifier?
            get() = program?.location

        override fun texture(index: Int, binding: GlTextureBinding): FromInstance {
            textures[index] = binding
            return this
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FromLocation) return false

            if (location != other.location) return false
            if (uniforms != other.uniforms) return false
            if (!textures.contentEquals(other.textures)) return false
            if (program != other.program) return false

            return true
        }

        override fun hashCode(): Int {
            var result = location.hashCode()
            result = 31 * result + uniforms.hashCode()
            result = 31 * result + textures.contentHashCode()
            result = 31 * result + program.hashCode()
            return result
        }
    }

    object Disabled : GlShaderShard {
        override val program: GlProgram? = null
        override val uniforms: Consumer<GlBoundProgram> = Consumer { }
        override val textures: Array<GlTextureBinding?> = Array(12) { null }
        override val location: NeoIdentifier? = null
    }
}
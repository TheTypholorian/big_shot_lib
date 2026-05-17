package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.FileNotFoundException

interface GlShaderShard : MaybeNamedResource, GlDrawStateShard {
    val program: GlProgram?
    val uniforms: GlBoundProgram.() -> Unit
    val textures: Map<String, GlTextureBinding>

    override fun bind(): GlBoundProgram {
        program?.let {
            val program = it.use()

            textures.forEach { (name, binding) ->
                program.setTexture(name, binding)
            }

            uniforms(program)

            return program
        }

        return object : GlBoundProgram {
            override fun setUniform(
                name: String,
                value: GlUniform.() -> Unit
            ) {
            }

            override fun setTexture(
                name: String,
                binding: GlTextureBinding
            ) {
            }

            override fun setTextureArray(
                name: String,
                vararg bindings: GlTextureBinding
            ) {
            }

            override val resource: GlProgram
                get() = throw NullPointerException()
            override val handle: GlStateStack.Handle<Int>
                get() = throw NullPointerException()
        }
    }

    fun texture(name: String, binding: GlTextureBinding): GlShaderShard {
        (textures as MutableMap<String, GlTextureBinding>)[name] = binding
        return this
    }

    data class NoShader(
        override val textures: Map<String, GlTextureBinding>
    ) : GlShaderShard {
        @SafeVarargs
        constructor(
            vararg textures: Pair<String, GlTextureBinding>
        ) : this(mutableMapOf(*textures))

        override val program: GlProgram
            get() = throw NullPointerException("No shader")
        override val uniforms: GlBoundProgram.() -> Unit = {}
        override val location: NeoIdentifier? = null

        override fun texture(name: String, binding: GlTextureBinding): NoShader {
            (textures as MutableMap<String, GlTextureBinding>)[name] = binding
            return this
        }
    }

    data class FromLocation @JvmOverloads constructor(
        override val location: NeoIdentifier,
        override val uniforms: GlBoundProgram.() -> Unit = { },
        override val textures: Map<String, GlTextureBinding>
    ) : GlShaderShard {
        @SafeVarargs
        @JvmOverloads
        constructor(
            location: NeoIdentifier,
            uniforms: GlBoundProgram.() -> Unit = { },
            vararg textures: Pair<String, GlTextureBinding>
        ) : this(location, uniforms, mutableMapOf(*textures))

        override val program: GlProgram
            // TODO
            get() = NeoShaderLoader[location] ?: throw FileNotFoundException("Couldn't find shader program $location")

        override fun texture(name: String, binding: GlTextureBinding): FromLocation {
            (textures as MutableMap<String, GlTextureBinding>)[name] = binding
            return this
        }
    }

    data class FromInstance @JvmOverloads constructor(
        private val getter: () -> GlProgram?,
        override val uniforms: GlBoundProgram.() -> Unit = { },
        override val textures: Map<String, GlTextureBinding>
    ) : GlShaderShard {
        @SafeVarargs
        @JvmOverloads
        constructor(
            getter: () -> GlProgram?,
            uniforms: GlBoundProgram.() -> Unit = { },
            vararg textures: Pair<String, GlTextureBinding>
        ) : this(getter, uniforms, mutableMapOf(*textures))

        override val program: GlProgram?
            get() = getter()
        override val location: NeoIdentifier?
            get() = program?.location

        override fun texture(name: String, binding: GlTextureBinding): FromInstance {
            (textures as MutableMap<String, GlTextureBinding>)[name] = binding
            return this
        }
    }

    object Disabled : GlShaderShard {
        override val program: GlProgram? = null
        override val uniforms: GlBoundProgram.() -> Unit = { }
        override val textures: Map<String, GlTextureBinding> = mapOf()
        override val location: NeoIdentifier? = null
    }
}
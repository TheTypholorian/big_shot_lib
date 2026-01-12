package net.typho.big_shot_lib.api.impl

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.DirectSampler
import net.typho.big_shot_lib.api.DirectUniform
import net.typho.big_shot_lib.api.IShader
import net.typho.big_shot_lib.error.ShaderCompileException
import net.typho.big_shot_lib.error.ShaderLinkException
import net.typho.big_shot_lib.gl.resource.ExtraUnbind
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.resource.ShaderType
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo
import net.typho.big_shot_lib.spirv.ShaderMixinCallback
import org.lwjgl.opengl.ARBGLSPIRV.GL_SHADER_BINARY_FORMAT_SPIR_V_ARB
import org.lwjgl.opengl.ARBGLSPIRV.glSpecializeShaderARB
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL41.glShaderBinary
import org.lwjgl.system.MemoryUtil
import java.util.*

open class NeoShader(
    private var locations: ShaderLocationsInfo?,
    val hasGeometryShader: Boolean,
    protected val location: ResourceLocation,
    protected val id: Int,
    protected val format: VertexFormat
) : IShader, ExtraUnbind {
    companion object {
        const val PATH = "neo/shaders"
        @JvmField
        val REGISTRY = HashMap<ResourceLocation, NeoShader>()

        @JvmStatic
        fun register(shader: NeoShader) {
            REGISTRY[shader.location()] = shader
        }

        @JvmStatic
        fun get(location: ResourceLocation) = REGISTRY.get(location)
    }

    init {
        type().label(id(), location().toString())
    }

    protected val uniforms = HashMap<String, DirectUniform?>()
    protected val samplers = HashMap<String, DirectSampler?>()

    override fun getLocations() = locations

    override fun setLocations(locations: ShaderLocationsInfo?) {
        this.locations = locations
    }

    override fun release() {
        glDeleteProgram(id())
    }

    override fun unbind() {
        super.unbind()
        unbindExtra()
    }

    override fun unbindExtra() {
        for (sampler in samplers.values) {
            sampler?.let {
                GlResourceType.SAMPLERS[it.unit].unbind()
            }
        }
    }

    override fun location() = location

    override fun type() = GlResourceType.PROGRAM

    override fun id(): Int = id

    override fun getUniform(name: String) = uniforms.computeIfAbsent(name) {
        val location = locations?.uniforms?.get(name) ?: glGetUniformLocation(id(), name)

        locations?.uniforms?.map?.put(name, location)

        if (location == -1) {
            return@computeIfAbsent null
        }

        return@computeIfAbsent DirectUniform(location)
    }

    override fun setSampler(name: String, id: Int) {
        samplers.computeIfAbsent(name) {
            getUniform(name)?.let {
                DirectSampler(it, samplers.size)
            }
        }?.set(id)
    }

    override fun vertexFormat() = format

    open class Builder(
        val location: ResourceLocation,
        val format: VertexFormat,
        val hasGeometryShader: Boolean
    ) {
        val locations = if (ShaderMixinCallback.enabled) ShaderLocationsInfo(hasGeometryShader) else null
        val sources = LinkedList<Int>()

        fun attach(
            type: ShaderType,
            fileName: String,
            source: String,
            entrypoint: String = "main"
        ) {
            val id = glCreateShader(type.id)

            if (ShaderMixinCallback.enabled) {
                val compiled = ShaderMixinCallback.compile(
                    location,
                    type,
                    format,
                    locations!!,
                    fileName,
                    IShader.resolveIncludes(source),
                    entrypoint
                )

                glShaderBinary(intArrayOf(id), GL_SHADER_BINARY_FORMAT_SPIR_V_ARB, compiled)
                glSpecializeShaderARB(id, "main", intArrayOf(), intArrayOf())

                MemoryUtil.memFree(compiled)
            } else {
                glShaderSource(id, IShader.resolveIncludes(source))
                glCompileShader(id)
            }

            if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
                throw ShaderCompileException("Error compiling ${type.key} shader for $location:\n${glGetShaderInfoLog(id).trim()}")
            }

            sources.add(id)
        }

        fun build(): NeoShader {
            val id = glCreateProgram()

            for (source in sources) {
                glAttachShader(id, source)
            }

            glLinkProgram(id)

            if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
                throw ShaderLinkException("Error linking shader $location:\n${glGetProgramInfoLog(id).trim()}")
            }

            for (source in sources) {
                glDetachShader(id, source)
                glDeleteShader(source)
            }

            return NeoShader(locations, hasGeometryShader, location, id, format)
        }
    }
}
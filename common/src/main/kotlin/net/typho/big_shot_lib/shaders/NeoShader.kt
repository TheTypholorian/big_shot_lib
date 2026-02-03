package net.typho.big_shot_lib.shaders

import com.google.gson.JsonObject
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.errors.ShaderCompileException
import net.typho.big_shot_lib.errors.ShaderLinkException
import net.typho.big_shot_lib.files.ResourceRegistry
import net.typho.big_shot_lib.resource.GlResourceType
import net.typho.big_shot_lib.shaders.mixins.ShaderLocationsInfo
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinManager
import org.lwjgl.opengl.GL20.*
import java.util.*

open class NeoShader(
    val hasGeometryShader: Boolean,
    protected val location: ResourceLocation,
    protected val id: Int,
    protected val format: VertexFormat
) : IShader {
    companion object : ResourceRegistry<NeoShader>("shaders") {
        override fun decode(
            id: ResourceLocation,
            json: JsonObject,
            manager: ResourceManager
        ): NeoShader {
            val format = json.get("format") ?: throw NullPointerException("Shader $id is missing vertex format")

            val builder = Builder(
                id,
                IShader.parseFormat(format)!!,
                json.has(ShaderType.GEOMETRY.key)
            )

            for (type in ShaderType.entries) {
                val sourceKeyJson = json.getAsJsonPrimitive(type.key)

                if (sourceKeyJson != null) {
                    val sourceKey = ResourceLocation.parse(sourceKeyJson.asString)
                    val withExtension = type.idConverter.idToFile(sourceKey)
                    val source = manager.getResourceOrThrow(withExtension)

                    source.openAsReader().use { sourceReader ->
                        builder.attach(
                            type,
                            withExtension.toString(),
                            sourceReader.readText()
                        )
                    }
                }
            }

            return builder.build()
        }
    }

    init {
        type().label(id(), location())
    }

    protected val uniforms = HashMap<String, DirectUniform?>()
    protected val samplers = HashMap<String, DirectSampler?>()

    override fun release() {
        glDeleteProgram(id())
    }

    override fun unbind() {
        super.unbind()

        for (sampler in samplers.values) {
            sampler?.let {
                GlResourceType.SAMPLERS[it.unit].unbind()
            }
        }
    }

    override fun canHotswapBind() = false

    override fun location() = location

    override fun type() = GlResourceType.PROGRAM

    override fun id(): Int = id

    override fun getUniform(name: String) = uniforms.computeIfAbsent(name) {
        val location = glGetUniformLocation(id(), name)

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
        val locations = if (ShaderMixinManager.enabled) ShaderLocationsInfo(format, hasGeometryShader) else null
        val sources = LinkedList<Int>()

        fun attach(
            type: ShaderType,
            fileName: String,
            source: String,
            entrypoint: String = ShaderMixinManager.DEFAULT_ENTRYPOINT
        ) {
            val id = glCreateShader(type.id)
            var newSource = IShader.resolveIncludes(source)

            if (ShaderMixinManager.enabled) {
                newSource = ShaderMixinManager.apply(
                    location,
                    type,
                    format,
                    locations!!,
                    fileName,
                    IShader.resolveIncludes(source),
                    entrypoint
                )
            }

            glShaderSource(id, newSource)
            glCompileShader(id)

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

            return NeoShader(hasGeometryShader, location, id, format)
        }
    }
}
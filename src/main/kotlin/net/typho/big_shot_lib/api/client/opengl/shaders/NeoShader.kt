package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniform
import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniformBufferPoint
import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.NeoUniform
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.errors.IllegalShaderSourceException
import net.typho.big_shot_lib.api.errors.MissingShaderSourceException
import net.typho.big_shot_lib.api.errors.ShaderCompileException
import org.lwjgl.opengl.GL20.GL_ACTIVE_UNIFORMS
import org.lwjgl.opengl.GL20.glGetProgrami
import org.lwjgl.opengl.GL31.glGetUniformBlockIndex
import org.lwjgl.opengl.GL31.glUniformBlockBinding

open class NeoShader(
    glId: Int,
    override val key: ShaderProgramKey
) : GlResource(glId, GlStateStack.shader), GlShader {
    companion object {
        @JvmField
        val NULL = NeoShader(0, ShaderProgramKey.NULL)
    }

    @JvmField
    protected val sources = HashMap<ShaderSourceType, Int>()
    @JvmField
    protected val uniforms = HashMap<String, GlUniform?>()
    @JvmField
    protected val uniformBuffers = HashMap<String, GlUniformBufferPoint?>()
    protected val uniformTypes: MutableMap<String, ShaderVariableType> by lazy {
        val map = HashMap<String, ShaderVariableType>()

        repeat(glGetProgrami(glId, GL_ACTIVE_UNIFORMS)) { i ->
            val pair = OpenGL.INSTANCE.getUniformInfo(glId, i)
            map[pair.first] = pair.second
        }

        return@lazy map
    }
    @JvmField
    protected val samplerUnits = HashMap<Int, Int>()
    override val location = key.location

    constructor(key: ShaderProgramKey) : this(OpenGL.INSTANCE.createShaderProgram(), key)

    override fun free() {
        OpenGL.INSTANCE.deleteShaderProgram(glId)
    }

    internal fun pickSamplerUnit(location: Int): Int {
        return samplerUnits.computeIfAbsent(location) { key ->
            repeat(samplerUnits.size) {
                if (!samplerUnits.containsValue(it)) {
                    return@computeIfAbsent it
                }
            }

            return@computeIfAbsent samplerUnits.size
        }
    }

    override fun getUniform(name: String): GlUniform? {
        return uniforms.computeIfAbsent(name) { key ->
            val location = OpenGL.INSTANCE.getUniformLocation(glId, key)

            if (location == -1) {
                return@computeIfAbsent null
            } else {
                return@computeIfAbsent NeoUniform(
                    name,
                    location,
                    uniformTypes[name]!!,
                    this
                )
            }
        }
    }

    override fun getUniformBuffer(name: String): GlUniformBufferPoint? {
        return uniformBuffers.computeIfAbsent(name) { key ->
            val index = glGetUniformBlockIndex(glId, name)

            if (index == -1) {
                return@computeIfAbsent null
            } else {
                val binding = uniformBuffers.count { it.value != null }
                glUniformBlockBinding(glId, index, binding)
                return@computeIfAbsent GlUniformBufferPoint(
                    name,
                    binding
                )
            }
        }
    }

    internal fun attach(type: ShaderSourceType, shader: Int) {
        if (!key.sources.contains(type)) {
            throw IllegalShaderSourceException("$this does not have a ${type.name.lowercase()} shader")
        }

        sources[type] = shader
    }

    internal fun attach(type: ShaderSourceType, code: String) {
        val glId = OpenGL.INSTANCE.createShaderSource(type)

        OpenGL.INSTANCE.shaderSourceCode(glId, code)
        OpenGL.INSTANCE.compileShaderSource(glId, type, key.location)

        attach(type, glId)
    }

    internal fun link() {
        val attached = HashSet<ShaderSourceType>()

        for (entry in sources) {
            OpenGL.INSTANCE.attachShaderSource(glId, entry.value)
            attached.add(entry.key)
        }

        val missing = HashSet(key.sources.keys)
        missing.removeAll(attached)

        if (missing.isNotEmpty()) {
            throw MissingShaderSourceException("$this is missing sources $missing")
        }

        OpenGL.INSTANCE.linkShaderProgram(glId, key.location)

        for (entry in sources) {
            OpenGL.INSTANCE.detachShaderSource(glId, entry.value)
            OpenGL.INSTANCE.deleteShaderSource(entry.value)
        }

        sources.clear()
    }

    override fun toString(): String {
        return "${type.name}(glId=$glId, location=${key.location})"
    }

    open class Builder(
        @JvmField
        val key: ShaderProgramKey,
        @JvmField
        protected val instance: NeoShader = NeoShader(key)
    ) {
        fun attach(type: ShaderSourceType, code: String, files: ShaderFileResolver) {
            val code = code.trim()

            if (code.startsWith("#version")) {
                throw ShaderCompileException("Big Shot shaders should not have #version in their code, put it in the shader json")
            }

            if (code.contains("#include")) {
                throw ShaderCompileException("Big Shot shaders do not use #include, they use shader modules which are better")
            }

            val builder = StringBuilder()

            builder.appendLine(key.version.versionString)

            for (module in key.modules) {
                module.loadGLSL(files, key, type)?.let { builder.appendLine(it) }
            }

            builder.appendLine(code)

            instance.attach(type, builder.toString())
        }

        fun build(): NeoShader {
            instance.link()
            return instance
        }
    }
}
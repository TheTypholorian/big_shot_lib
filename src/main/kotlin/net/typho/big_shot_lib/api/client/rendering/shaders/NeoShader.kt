package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.errors.IllegalShaderSourceException
import net.typho.big_shot_lib.api.client.rendering.errors.MissingShaderSourceException
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource

open class NeoShader(
    glId: Int,
    @JvmField
    val key: ShaderProgramKey
) : GlResource(glId), GlShader {
    companion object {
        @JvmField
        val NULL = NeoShader(0, ShaderProgramKey.NULL)
    }

    @JvmField
    protected val sources = HashMap<ShaderSourceType, Int>()
    @JvmField
    protected val uniforms = HashMap<String, GlUniform?>()
    @JvmField
    protected val uniformTypes = HashMap<String, SVTy>()
    @JvmField
    protected val samplerUnits = HashMap<Int, Int>()

    constructor(key: ShaderProgramKey) : this(OpenGL.INSTANCE.createShaderProgram(), key)

    override fun bind(glId: Int) {
        OpenGL.INSTANCE.bindShaderProgram(glId)
    }

    override fun free() {
        OpenGL.INSTANCE.deleteShaderProgram(glId)
    }

    override fun location() = key.location

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
                    this
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

        val missing = HashSet(key.sources)
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
        return "${javaClass.simpleName}(glId=$glId, location=${key.location})"
    }

    open class Builder(
        @JvmField
        val key: ShaderProgramKey,
        @JvmField
        protected val instance: NeoShader = NeoShader(key)
    ) {
        @JvmField
        protected val mixin = net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager.create(key)

        fun attach(type: ShaderSourceType, code: String, includes: ShaderFileResolver) {
            instance.attach(type, mixin.apply(type, includes.loadIncludes(code, ShaderSourceKey(
                key,
                type
            ).toString())))
        }

        fun build(): NeoShader {
            instance.link()
            return instance
        }
    }
}
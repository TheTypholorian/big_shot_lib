package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.errors.IllegalShaderSourceException
import net.typho.big_shot_lib.api.client.rendering.errors.MissingShaderSourceException
import net.typho.big_shot_lib.api.client.rendering.event.RenderData
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import net.typho.big_shot_lib.api.util.resources.Named
import org.lwjgl.glfw.GLFW.glfwGetTime

open class GlShader(
    glId: Int,
    @JvmField
    val key: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey
) : GlResource(glId), Named {
    companion object {
        @JvmField
        val NULL = GlShader(0, _root_ide_package_.net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey.NULL)
    }

    @JvmField
    protected val sources = HashMap<net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType, Int>()
    @JvmField
    protected val uniforms = HashMap<String, net.typho.big_shot_lib.api.client.rendering.shaders.GlUniform?>()
    @JvmField
    protected val samplerUnits = HashMap<Int, Int>()

    constructor(key: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey) : this(OpenGL.INSTANCE.createShaderProgram(), key)

    override fun bind(glId: Int) {
        OpenGL.INSTANCE.bindShaderProgram(glId)
    }

    override fun free() {
        OpenGL.INSTANCE.deleteShaderProgram(glId)
    }

    override fun location() = key.location

    fun pickSamplerUnit(location: Int): Int {
        return samplerUnits.computeIfAbsent(location) { key ->
            repeat(samplerUnits.size) {
                if (!samplerUnits.containsValue(it)) {
                    return@computeIfAbsent it
                }
            }

            return@computeIfAbsent samplerUnits.size
        }
    }

    fun setCommonUniforms(
        data: RenderData,
        time: Float = glfwGetTime().toFloat()
    ) {
        getUniform("GLFWTime")?.setValue(time)
        getUniform("ScreenSize")?.setValue(data.windowWidth.toFloat(), data.windowHeight.toFloat())
        getUniform("ProjMat")?.setValue(data.projMat)
        getUniform("ModelViewMat")?.setValue(data.modelViewMat)
    }

    fun getUniform(name: String): net.typho.big_shot_lib.api.client.rendering.shaders.GlUniform? {
        return uniforms.computeIfAbsent(name) { key ->
            val location = OpenGL.INSTANCE.getUniformLocation(glId, key)

            if (location == -1) {
                return@computeIfAbsent null
            } else {
                return@computeIfAbsent _root_ide_package_.net.typho.big_shot_lib.api.client.rendering.shaders.GlUniform(
                    location,
                    this
                )
            }
        }
    }

    internal fun attach(type: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType, shader: Int) {
        if (!key.sources.contains(type)) {
            throw IllegalShaderSourceException("$this does not have a ${type.name.lowercase()} shader")
        }

        sources[type] = shader
    }

    internal fun attach(type: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType, code: String) {
        val glId = OpenGL.INSTANCE.createShaderSource(type)

        OpenGL.INSTANCE.shaderSourceCode(glId, code)
        OpenGL.INSTANCE.compileShaderSource(glId, type, key.location)

        attach(type, glId)
    }

    internal fun link() {
        val attached = HashSet<net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType>()

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
        val key: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey,
        @JvmField
        protected val instance: GlShader = GlShader(key)
    ) {
        @JvmField
        protected val mixin = _root_ide_package_.net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager.create(key)

        fun attach(type: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType, code: String, includes: net.typho.big_shot_lib.api.client.rendering.shaders.ShaderFileResolver) {
            instance.attach(type, mixin.apply(type, includes.loadIncludes(code, _root_ide_package_.net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceKey(
                key,
                type
            ).toString())))
        }

        fun build(): GlShader {
            instance.link()
            return instance
        }
    }
}
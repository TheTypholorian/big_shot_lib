package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import net.typho.big_shot_lib.api.math.vec.NeoVec2i
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL33.glBindSampler
import kotlin.collections.map

open class NeoGlProgram(
    override val location: NeoIdentifier,
    override val format: NeoVertexFormat,
    glId: Int,
    autoFree: Boolean,
    context: RenderingContext = RenderingContext.get()
) : NeoGlResource(GlResourceType.PROGRAM, glId, autoFree, context), GlProgram {
    private val uniforms = hashMapOf<String, GlUniform?>()

    constructor(location: NeoIdentifier, format: NeoVertexFormat) : this(location, format, GlResourceType.PROGRAM.create(), true)

    override fun use(): GlBoundProgram {
        checkUsable()
        InternalUtil.INSTANCE.onBind(this)
        val handle = NeoGlStateManager.CURRENT.program.push(glId)
        return object : GlBoundProgram {
            override val resource: GlProgram = this@NeoGlProgram
            override val handle: GlStateStack.Handle<Int> = handle
            val initialTextureUnit = NeoGlStateManager.CURRENT.activeTexture
            val usedUnits = hashSetOf<Int>()

            override fun setUniform(
                name: String,
                value: GlUniform.() -> Unit
            ) {
                assertBound {
                    uniforms.computeIfAbsent(name) { key ->
                        val location = glGetUniformLocation(glId, key)
                        return@computeIfAbsent if (location == -1) null else GlUniform.Basic(location)
                    }?.let(value)
                }
            }

            override fun setTexture(
                unit: Int,
                binding: GlTextureBinding
            ) {
                assertBound {
                    val name = binding.uniformName ?: "Sampler$unit"
                    val texture = binding.texture

                    NeoGlStateManager.CURRENT.activeTexture = unit
                    glBindTexture(binding.target.glId, texture.glId)
                    glBindSampler(unit, binding.sampler?.glId ?: 0)
                    setUniform(name) { set(unit) }
                    setUniform("${name}Size") { set(texture.width, texture.height) }

                    usedUnits.add(unit)
                }
            }

            override fun setTextureArray(
                unit: Int,
                name: String,
                vararg bindings: GlTextureBinding
            ) {
                assertBound {
                    var unitInc = unit

                    for (binding in bindings) {
                        val texture = binding.texture
                        val unit = unitInc++

                        NeoGlStateManager.CURRENT.activeTexture = unit
                        glBindTexture(binding.target.glId, texture.glId)
                        glBindSampler(unit, binding.sampler?.glId ?: 0)

                        usedUnits.add(unit)
                    }

                    setUniform(name) { set(IntArray(bindings.size) { it + unit }) }
                    setUniform("${name}Sizes") { setIntVecs(bindings.map { NeoVec2i(it.texture.width, it.texture.height) }.toTypedArray()) }
                }
            }

            override fun unbind() {
                super.unbind()
                NeoGlStateManager.CURRENT.activeTexture = initialTextureUnit
                usedUnits.forEach { glBindSampler(it, 0) }
                InternalUtil.INSTANCE.onUnbind(resource)
            }
        }
    }

    override fun attach(shader: GlShader) {
        glAttachShader(glId, shader.glId)
    }

    override fun detach(shader: GlShader) {
        glDetachShader(glId, shader.glId)
    }

    override fun getInfoLog(): String {
        return glGetProgramInfoLog(glId, 4096).trim()
    }

    override fun link(): Boolean {
        format.elements.forEachIndexed { index, element ->
            glBindAttribLocation(glId, index, format.getElementName(element))
        }

        glLinkProgram(glId)

        return glGetProgrami(glId, GL_LINK_STATUS) == GL_TRUE
    }

    override fun validate(): Boolean {
        glValidateProgram(glId)

        return glGetProgrami(glId, GL_VALIDATE_STATUS) == GL_TRUE
    }
}
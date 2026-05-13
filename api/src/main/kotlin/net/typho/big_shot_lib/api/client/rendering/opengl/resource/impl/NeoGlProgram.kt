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
import org.lwjgl.opengl.GL30.glBindBufferBase
import org.lwjgl.opengl.GL30.glBindBufferRange
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER
import org.lwjgl.opengl.GL31.glGetUniformBlockIndex
import org.lwjgl.opengl.GL31.glUniformBlockBinding
import org.lwjgl.opengl.GL33.glBindSampler
import org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BLOCK
import org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER
import org.lwjgl.opengl.GL43.glGetProgramResourceIndex
import org.lwjgl.opengl.GL43.glShaderStorageBlockBinding
import kotlin.collections.map

open class NeoGlProgram(
    override val location: NeoIdentifier,
    override val format: NeoVertexFormat,
    glId: Int,
    autoFree: Boolean,
    context: RenderingContext = RenderingContext.get()
) : NeoGlResource(GlResourceType.PROGRAM, glId, autoFree, context), GlProgram {
    private val uniforms = hashMapOf<String, GlUniform?>()
    private val uniformBuffers = hashMapOf<String, Int?>()
    private val shaderStorageBuffers = hashMapOf<String, Int?>()

    constructor(location: NeoIdentifier, format: NeoVertexFormat) : this(location, format, GlResourceType.PROGRAM.create(), true)

    protected fun getUniformBuffer(name: String): Int? = uniformBuffers.computeIfAbsent(name) {
        val index = glGetUniformBlockIndex(glId, name)

        if (index == -1) {
            return@computeIfAbsent null
        } else {
            val binding = uniformBuffers.count { it.value != null }
            glUniformBlockBinding(glId, index, binding)
            return@computeIfAbsent binding
        }
    }

    protected fun getShaderStorageBuffer(name: String): Int? = shaderStorageBuffers.computeIfAbsent(name) {
        val index = glGetProgramResourceIndex(glId, GL_SHADER_STORAGE_BLOCK, name)

        if (index == -1) {
            return@computeIfAbsent null
        } else {
            val binding = shaderStorageBuffers.count { it.value != null }
            glShaderStorageBlockBinding(glId, index, binding)
            return@computeIfAbsent binding
        }
    }

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

            override fun setUniformBuffer(name: String, glId: Int) {
                glBindBufferBase(GL_UNIFORM_BUFFER, getUniformBuffer(name) ?: throw NullPointerException("No uniform buffer $name"), glId)
            }

            override fun setUniformBufferRange(
                name: String,
                glId: Int,
                offset: Long,
                length: Long
            ) {
                glBindBufferRange(GL_UNIFORM_BUFFER, getUniformBuffer(name) ?: throw NullPointerException("No uniform buffer $name"), glId, offset, length)
            }

            override fun setShaderStorageBuffer(name: String, glId: Int) {
                glBindBufferBase(GL_SHADER_STORAGE_BUFFER, getShaderStorageBuffer(name) ?: throw NullPointerException("No shader storage buffer $name"), glId)
            }

            override fun setShaderStorageBufferRange(
                name: String,
                glId: Int,
                offset: Long,
                length: Long
            ) {
                glBindBufferRange(GL_SHADER_STORAGE_BUFFER, getShaderStorageBuffer(name) ?: throw NullPointerException("No shader storage buffer $name"), glId, offset, length)
            }

            override fun setTexture(
                unit: Int,
                binding: GlTextureBinding
            ) {
                assertBound {
                    val name = binding.uniformName ?: "Sampler$unit"
                    val texture = binding.texture

                    NeoGlStateManager.CURRENT.activeTexture = unit
                    NeoGlStateManager.CURRENT.rawBindTexture(binding.target, texture.glId)
                    glBindSampler(unit, binding.sampler?.glId ?: 0)
                    setUniform(name) { set(unit) }
                    setUniform("${name}Size") { set(texture.width!!, texture.height!!) }

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
                        NeoGlStateManager.CURRENT.rawBindTexture(binding.target, texture.glId)
                        glBindSampler(unit, binding.sampler?.glId ?: 0)

                        usedUnits.add(unit)
                    }

                    setUniform(name) { set(IntArray(bindings.size) { it + unit }) }
                    setUniform("${name}Sizes") { setIntVecs(bindings.map { NeoVec2i(it.texture.width!!, it.texture.height!!) }.toTypedArray()) }
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
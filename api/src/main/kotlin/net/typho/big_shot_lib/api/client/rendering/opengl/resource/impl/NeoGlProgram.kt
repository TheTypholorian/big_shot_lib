package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL33.glBindSampler

class NeoGlProgram(
    override val location: NeoIdentifier,
    glId: Int,
    autoFree: Boolean
) : NeoGlResource(GlResourceType.PROGRAM, glId, autoFree), GlProgram {
    private val uniforms = hashMapOf<String, GlUniform?>()

    constructor(location: NeoIdentifier) : this(location, GlResourceType.PROGRAM.create(), true)

    override fun use(): GlBoundProgram {
        return object : GlBoundProgram {
            override val resource: GlProgram = this@NeoGlProgram
            override val handle: GlStateStack.Handle<Int>
                get() = NeoGlStateManager.INSTANCE.program.push(glId)
            val initialTextureUnit = NeoGlStateManager.INSTANCE.activeTexture

            override fun setUniform(
                name: String,
                value: GlUniform.() -> Unit
            ) {
                uniforms.computeIfAbsent(name) { key ->
                    val location = glGetUniformLocation(glId, key)
                    return@computeIfAbsent if (location == -1) null else GlUniform.Basic(location)
                }?.let(value)
            }

            override fun setTexture(
                name: String,
                unit: Int,
                target: GlTextureTarget,
                glId: Int,
                samplerId: Int
            ) {
                glActiveTexture(unit)
                glBindTexture(target.glId, glId)
                glBindSampler(unit, samplerId)
                setUniform(name) { set(unit) }
            }

            override fun unbind() {
                super.unbind()
                NeoGlStateManager.INSTANCE.activeTexture = initialTextureUnit
            }
        }
    }

    override fun attach(shader: GlShader) {
        glAttachShader(glId, shader.glId)
    }

    override fun detach(shader: GlShader) {
        glDetachShader(glId, shader.glId)
    }

    override fun link(onError: (log: String) -> Nothing) {
        glLinkProgram(glId)

        if (glGetProgrami(glId, GL_LINK_STATUS) == GL_FALSE) {
            onError(glGetProgramInfoLog(glId, 4096).trim())
        }
    }

    override fun validate(onError: (log: String) -> Nothing) {
        glValidateProgram(glId)

        if (glGetProgrami(glId, GL_VALIDATE_STATUS) == GL_FALSE) {
            onError(glGetProgramInfoLog(glId, 4096).trim())
        }
    }
}
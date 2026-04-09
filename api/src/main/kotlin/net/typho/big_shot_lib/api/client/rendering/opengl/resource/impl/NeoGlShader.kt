package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*

open class NeoGlShader(
    override val location: NeoIdentifier,
    override val shaderType: GlShaderType,
    glId: Int,
    autoFree: Boolean,
    context: RenderingContext = RenderingContext.get()
) : NeoGlResource(shaderType.resourceType, glId, autoFree, context), GlShader {
    override var source: String
        get() = glGetShaderSource(glId)
        set(value) = glShaderSource(glId, value)

    constructor(location: NeoIdentifier, shaderType: GlShaderType) : this(location, shaderType, shaderType.resourceType.create(), true)

    override fun getInfoLog(): String {
        return glGetShaderInfoLog(glId, 4096).trim()
    }

    override fun compile(): Boolean {
        glCompileShader(glId)

        return glGetShaderi(glId, GL_COMPILE_STATUS) == GL_TRUE
    }
}
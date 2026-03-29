package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*

class NeoGlShader(
    override val location: NeoIdentifier,
    override val shaderType: GlShaderType,
    glId: Int,
    autoFree: Boolean
) : NeoGlResource(shaderType.resourceType, glId, autoFree), GlShader {
    override var source: String
        get() = glGetShaderSource(glId)
        set(value) = glShaderSource(glId, value)

    constructor(location: NeoIdentifier, shaderType: GlShaderType) : this(location, shaderType, shaderType.resourceType.create(), true)

    override fun compile(onError: (log: String) -> Nothing) {
        glCompileShader(glId)

        if (glGetShaderi(glId, GL_COMPILE_STATUS) == GL_FALSE) {
            onError(glGetShaderInfoLog(glId, 4096).trim())
        }
    }
}
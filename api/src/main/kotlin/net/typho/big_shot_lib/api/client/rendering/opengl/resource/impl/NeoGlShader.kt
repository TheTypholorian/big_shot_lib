package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*

class NeoGlShader(
    override val location: NeoIdentifier,
    override val shaderType: GlShaderType,
    glId: Int
) : NeoGlResource(shaderType.resourceType, glId), GlShader {
    override val type: GlResourceType
        get() = shaderType.resourceType
    override var source: String
        get() = glGetShaderSource(glId)
        set(value) = glShaderSource(glId, value)

    constructor(location: NeoIdentifier, shaderType: GlShaderType) : this(location, shaderType, shaderType.resourceType.create())

    override fun compile(onError: (log: String) -> Nothing) {
        glCompileShader(glId)

        if (glGetShaderi(glId, GL_COMPILE_STATUS) == GL_FALSE) {
            onError(glGetShaderInfoLog(glId, 4096).trim())
        }
    }
}
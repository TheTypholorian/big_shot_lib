package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*

open class NeoGlShader(
    override val location: Identifier,
    override val shaderType: GlShaderType,
    glId: Int
) : NeoGlResource(shaderType.resourceType, glId), GlShader {
    override var source: String
        get() = glGetShaderSource(glId)
        set(value) = glShaderSource(glId, value)

    constructor(location: Identifier, shaderType: GlShaderType) : this(location, shaderType, shaderType.resourceType.create())

    override fun getInfoLog(): String {
        return glGetShaderInfoLog(glId, 4096).trim()
    }

    override fun compile(): Boolean {
        glCompileShader(glId)

        return glGetShaderi(glId, GL_COMPILE_STATUS) == GL_TRUE
    }
}
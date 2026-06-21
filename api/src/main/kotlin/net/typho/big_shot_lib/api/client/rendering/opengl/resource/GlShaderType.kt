package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER

enum class GlShaderType(
    override val glId: Int,
    @JvmField
    val resourceType: GlResourceType,
    @JvmField
    val extension: String
): GlConstant {
    VERTEX(GL_VERTEX_SHADER, GlResourceType.VERTEX_SHADER, ".vsh"),
    GEOMETRY(GL_GEOMETRY_SHADER, GlResourceType.GEOMETRY_SHADER, ".gsh"),
    FRAGMENT(GL_FRAGMENT_SHADER, GlResourceType.FRAGMENT_SHADER, ".fsh")
}
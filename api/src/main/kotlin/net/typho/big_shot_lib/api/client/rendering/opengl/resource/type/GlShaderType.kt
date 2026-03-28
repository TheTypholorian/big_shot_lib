package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER
import org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER
import org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER

enum class GlShaderType(
    override val glId: Int,
    @JvmField
    val resourceType: GlResourceType,
    @JvmField
    val extension: String
): GlConstant {
    VERTEX(GL_VERTEX_SHADER, GlResourceType.VERTEX_SHADER, "vsh"),
    GEOMETRY(GL_GEOMETRY_SHADER, GlResourceType.GEOMETRY_SHADER, "gsh"),
    FRAGMENT(GL_FRAGMENT_SHADER, GlResourceType.FRAGMENT_SHADER, "fsh"),
    TESS_CONTROL(GL_TESS_CONTROL_SHADER, GlResourceType.TESS_CONTROL_SHADER, "tcsh"),
    TESS_EVALUATION(GL_TESS_EVALUATION_SHADER, GlResourceType.TESS_EVALUATION_SHADER, "tesh"),
    COMPUTE(GL_COMPUTE_SHADER, GlResourceType.COMPUTE_SHADER, "csh")
}
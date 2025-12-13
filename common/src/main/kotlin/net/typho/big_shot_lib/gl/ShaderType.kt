package net.typho.big_shot_lib.gl

import net.minecraft.resources.FileToIdConverter
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL43.*

enum class ShaderType(
    val id: Int,
    val key: String,
    val extension: String,
    val idConverter: FileToIdConverter = FileToIdConverter("neo/shaders", ".$extension")
) {
    VERTEX(GL_VERTEX_SHADER, "vertex", "vsh"),
    GEOMETRY(GL_GEOMETRY_SHADER, "geometry", "gsh"),
    FRAGMENT(GL_FRAGMENT_SHADER, "fragment", "fsh"),
    COMPUTE(GL_COMPUTE_SHADER, "compute", "csh"),
    TESS_CONTROL(GL_TESS_CONTROL_SHADER, "tessellation_control", "tcsh"),
    TESS_EVALUATION(GL_TESS_EVALUATION_SHADER, "tessellation_evaluation", "tesh"),
}
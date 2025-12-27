package net.typho.big_shot_lib.gl.resource

import com.mojang.blaze3d.shaders.Program
import net.minecraft.resources.FileToIdConverter
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.util.shaderc.Shaderc.shaderc_glsl_fragment_shader
import org.lwjgl.util.shaderc.Shaderc.shaderc_glsl_vertex_shader

enum class ShaderType(
    val id: Int,
    val shadercId: Int,
    val key: String,
    val extension: String,
    val idConverter: FileToIdConverter = FileToIdConverter("neo/shaders", ".$extension")
) {
    VERTEX(GL_VERTEX_SHADER, shaderc_glsl_vertex_shader, "vertex", "vsh"),
    //GEOMETRY(GL_GEOMETRY_SHADER, shaderc_glsl_geometry_shader, "geometry", "gsh"),
    FRAGMENT(GL_FRAGMENT_SHADER, shaderc_glsl_fragment_shader, "fragment", "fsh");

    override fun toString() = key

    companion object {
        @JvmStatic
        fun fromVanillaType(type: Program.Type) = when (type) {
            Program.Type.VERTEX -> VERTEX
            Program.Type.FRAGMENT -> FRAGMENT
        }
    }
}
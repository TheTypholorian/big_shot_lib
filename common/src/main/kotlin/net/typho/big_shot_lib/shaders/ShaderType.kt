package net.typho.big_shot_lib.shaders

import com.mojang.blaze3d.shaders.Program
import net.minecraft.resources.FileToIdConverter
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL32
import org.lwjgl.util.shaderc.Shaderc
import org.lwjgl.util.spvc.Spv

enum class ShaderType(
    val id: Int,
    val shadercId: Int,
    val spvcId: Int,
    val key: String,
    val extension: String,
    val idConverter: FileToIdConverter = FileToIdConverter("neo/shaders", ".$extension")
) {
    VERTEX(GL20.GL_VERTEX_SHADER, Shaderc.shaderc_glsl_vertex_shader, Spv.SpvExecutionModelVertex, "vertex", "vsh"),
    GEOMETRY(GL32.GL_GEOMETRY_SHADER, Shaderc.shaderc_glsl_geometry_shader, Spv.SpvExecutionModelGeometry, "geometry", "gsh"),
    FRAGMENT(GL20.GL_FRAGMENT_SHADER, Shaderc.shaderc_glsl_fragment_shader, Spv.SpvExecutionModelFragment, "fragment", "fsh");

    override fun toString() = key

    companion object {
        @JvmStatic
        fun fromVanillaType(type: Program.Type) = when (type) {
            Program.Type.VERTEX -> VERTEX
            Program.Type.FRAGMENT -> FRAGMENT
        }
    }
}
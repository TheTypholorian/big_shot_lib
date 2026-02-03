package net.typho.big_shot_lib.gl.resource

import com.mojang.blaze3d.shaders.CompiledShader
import net.minecraft.resources.FileToIdConverter
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER
import org.lwjgl.util.shaderc.Shaderc.*
import org.lwjgl.util.spvc.Spv.*

enum class ShaderType(
    val id: Int,
    val shadercId: Int,
    val spvcId: Int,
    val key: String,
    val extension: String,
    val idConverter: FileToIdConverter = FileToIdConverter("neo/shaders", ".$extension")
) {
    VERTEX(GL_VERTEX_SHADER, shaderc_glsl_vertex_shader, SpvExecutionModelVertex, "vertex", "vsh"),
    GEOMETRY(GL_GEOMETRY_SHADER, shaderc_glsl_geometry_shader, SpvExecutionModelGeometry, "geometry", "gsh"),
    FRAGMENT(GL_FRAGMENT_SHADER, shaderc_glsl_fragment_shader, SpvExecutionModelFragment, "fragment", "fsh");

    override fun toString() = key

    companion object {
        @JvmStatic
        fun fromVanillaType(type: CompiledShader.Type) = when (type) {
            CompiledShader.Type.VERTEX -> VERTEX
            CompiledShader.Type.FRAGMENT -> FRAGMENT
        }
    }
}
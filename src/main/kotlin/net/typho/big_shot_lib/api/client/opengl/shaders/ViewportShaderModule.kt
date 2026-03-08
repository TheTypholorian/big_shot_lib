package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL

/**
 * Provides an ivec2 uniform `Viewport` with x and y being current width and height
 */
object ViewportShaderModule : ShaderModule.UnitType<ViewportShaderModule>() {
    override val dependencies: Set<Pair<ShaderModule.Type<*>, () -> ShaderModule?>> = setOf()
    override val location = BigShotApi.id("viewport")

    override fun setUniforms(arguments: RenderArguments, shader: GlShader): GlBindResult {
        val viewport = OpenGL.INSTANCE.getViewport()
        shader.getUniform("Viewport")?.setValue(viewport.width, viewport.height)
        return GlBindResult.Success
    }

    override fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String {
        return "uniform ivec2 Viewport;"
    }
}
package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import org.lwjgl.glfw.GLFW.glfwGetTime

/**
 * Provides a float uniform `GLFWTime`, with 1f = 1 second.
 * Counts from launch, and does not stop.
 */
object TimeShaderModule : ShaderModule.UnitType<TimeShaderModule>() {
    override val dependencies: Set<Pair<ShaderModule.Type<*>, () -> ShaderModule?>> = setOf()
    override val location = BigShotApi.id("glfw_time")

    override fun setUniforms(arguments: RenderArguments, shader: GlShader): GlBindResult {
        shader.getUniform("GLFWTime")?.setValue(glfwGetTime())
        return GlBindResult.Success
    }

    override fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String {
        return "uniform float GLFWTime;"
    }
}
package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.event.RenderData
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.Named
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.system.NativeResource

interface GlShader : GlBindable, Named, NativeResource {
    fun key(): ShaderProgramKey

    fun getUniform(name: String): GlUniform?

    fun setCommonUniforms(
        data: RenderData,
        time: Float = glfwGetTime().toFloat()
    ) {
        getUniform("GLFWTime")?.setValue(time)
        getUniform("ScreenSize")?.setValue(data.windowWidth.toFloat(), data.windowHeight.toFloat())
        getUniform("ProjMat")?.setValue(data.projMat)
        getUniform("ModelViewMat")?.setValue(data.modelViewMat)
    }
}
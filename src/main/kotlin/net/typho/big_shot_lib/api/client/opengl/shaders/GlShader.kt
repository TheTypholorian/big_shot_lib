package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniform
import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniformBufferPoint
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.client.util.events.RenderEventData
import net.typho.big_shot_lib.api.util.resources.NamedResource
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.system.NativeResource

interface GlShader : GlBindable, NamedResource, NativeResource {
    fun key(): ShaderProgramKey

    fun getUniform(name: String): GlUniform?

    fun getUniformBuffer(name: String): GlUniformBufferPoint?

    fun setCommonUniforms(
        data: RenderEventData,
        time: Float = glfwGetTime().toFloat()
    ) {
        getUniform("GLFWTime")?.setValue(time)
        getUniform("ScreenSize")?.setValue(data.target.width.toFloat(), data.target.height.toFloat())
        getUniform("ProjMat")?.setValue(data.projMat)
        getUniform("ModelViewMat")?.setValue(data.modelViewMat)
    }
}
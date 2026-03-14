package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniform
import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniformBufferPoint
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.util.BoundResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.util.resources.NamedResource
import org.lwjgl.system.NativeResource

interface GlShader : NamedResource, NativeResource {
    val key: ShaderProgramKey

    fun bind(tracker: GlStateTracker = OpenGL.INSTANCE): Bound

    interface Bound : BoundResource {
        val shader: GlShader

        fun getUniform(name: String): GlUniform?

        fun getUniformBuffer(name: String): GlUniformBufferPoint?
    }
}
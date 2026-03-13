package net.typho.big_shot_lib.api.client.opengl.shaders

import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniform
import net.typho.big_shot_lib.api.client.opengl.shaders.uniforms.GlUniformBufferPoint
import net.typho.big_shot_lib.api.util.resources.NamedResource
import org.lwjgl.system.NativeResource

interface GlShader : GlBindable, NamedResource, NativeResource {
    val key: ShaderProgramKey

    fun getUniform(name: String): GlUniform?

    fun getUniformBuffer(name: String): GlUniformBufferPoint?
}
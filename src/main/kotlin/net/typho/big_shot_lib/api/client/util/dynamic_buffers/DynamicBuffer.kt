package net.typho.big_shot_lib.api.client.util.dynamic_buffers

import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebufferAttachment
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface DynamicBuffer<M : ShaderMixin> : GlFramebufferAttachment, ShaderMixin.Factory<M> {
    val location: ResourceIdentifier
    val blend: Boolean
    var shaderLocation: Int?
}
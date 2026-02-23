package net.typho.big_shot_lib.api.client.util.dynamic_buffers

import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebufferAttachment
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface DynamicBuffer : GlFramebufferAttachment, ShaderMixin.Factory {
    fun setShaderLocation(location: Int)

    fun location(): ResourceIdentifier

    fun blend(): Boolean
}
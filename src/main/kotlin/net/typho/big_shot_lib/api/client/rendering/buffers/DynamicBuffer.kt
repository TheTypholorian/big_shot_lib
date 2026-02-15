package net.typho.big_shot_lib.api.client.rendering.buffers

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebufferAttachment
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface DynamicBuffer : GlFramebufferAttachment, ShaderMixin.Factory {
    fun location(): ResourceIdentifier

    fun blend(): Boolean
}
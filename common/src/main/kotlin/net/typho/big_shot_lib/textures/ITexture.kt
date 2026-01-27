package net.typho.big_shot_lib.textures

import net.typho.big_shot_lib.framebuffers.IFramebufferAttachment
import net.typho.big_shot_lib.gl.InterpolationType
import net.typho.big_shot_lib.gl.resource.GlResourceInstance

interface ITexture : GlResourceInstance, IFramebufferAttachment {
    fun setInterpolation(interpolation: InterpolationType)
}
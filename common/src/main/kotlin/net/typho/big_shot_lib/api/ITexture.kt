package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.InterpolationType
import net.typho.big_shot_lib.gl.resource.GlResourceInstance

interface ITexture : GlResourceInstance, IFramebufferAttachment {
    fun setInterpolation(interpolation: InterpolationType)
}
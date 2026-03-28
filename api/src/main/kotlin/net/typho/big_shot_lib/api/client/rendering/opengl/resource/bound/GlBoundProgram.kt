package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform

interface GlBoundProgram : GlBoundResource<GlProgram> {
    fun setUniform(name: String, value: (uniform: GlUniform) -> Unit)

    fun setTexture(name: String, unit: Int, target: GlTextureTarget, glId: Int, samplerId: Int = 0)

    fun setTexture(name: String, unit: Int, target: GlTextureTarget, texture: GlTexture2D, sampler: GlSampler? = null) {
        setTexture(name, unit, target, texture.glId, sampler?.glId ?: 0)
    }
}
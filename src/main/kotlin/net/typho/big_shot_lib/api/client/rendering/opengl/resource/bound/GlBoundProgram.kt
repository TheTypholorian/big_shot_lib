package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform

interface GlBoundProgram : GlBoundResource<GlProgram> {
    fun setUniform(name: String, value: (uniform: GlUniform) -> Unit)

    fun setTexture(name: String, target: GlTextureTarget, glId: Int, samplerId: Int = 0)

    fun setTexture(name: String, target: GlTextureTarget, texture: GlTexture, samplerId: Int = 0) {
        setTexture(name, target, texture.glId, samplerId)
    }
}
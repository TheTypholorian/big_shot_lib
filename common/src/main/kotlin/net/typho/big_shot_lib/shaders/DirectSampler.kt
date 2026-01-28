package net.typho.big_shot_lib.shaders

import net.typho.big_shot_lib.gl.resource.GlResourceType

open class DirectSampler(
    val uniform: DirectUniform,
    val unit: Int
) {
    fun set(id: Int) {
        GlResourceType.SAMPLERS[unit].bind(id)
        uniform.set(unit)
    }
}
package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.resource.GlResourceType

open class DirectSampler(
    val uniform: DirectUniform,
    val unit: Int
) {
    fun set(id: Int) {
        GlResourceType.Companion.SAMPLERS[unit].bind(id)
        uniform.set(unit)
    }
}
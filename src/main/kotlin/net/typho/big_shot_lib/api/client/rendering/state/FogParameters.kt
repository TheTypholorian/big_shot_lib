package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.shaders.GlShader
import net.typho.big_shot_lib.api.util.IColor

@JvmRecord
data class FogParameters(
    @JvmField
    val start: Float,
    @JvmField
    val end: Float,
    @JvmField
    val shape: FogShape,
    @JvmField
    val color: IColor
) {
    fun upload(shader: GlShader) {
        shader.getUniform("FogStart")?.setValue(start)
        shader.getUniform("FogEnd")?.setValue(end)
        shader.getUniform("FogShape")?.setValue(shape.ordinal)
        shader.getUniform("FogColor")?.setValue(color)
    }

    fun interface Supplier {
        fun get(): FogParameters
    }

    companion object {
        @JvmField
        val INSTANCE: Supplier = Supplier::class.loadService()
    }
}

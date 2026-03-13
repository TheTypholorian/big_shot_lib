package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.opengl.state.GlTextureType
import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import net.typho.big_shot_lib.api.client.opengl.util.InterpolationType
import net.typho.big_shot_lib.api.client.opengl.util.TextureComparisonMode
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import org.lwjgl.system.NativeResource

interface GlTexture : GlNamed, NativeResource {
    val format: TextureFormat
    val type: GlTextureType

    fun bind(): Bound<*>

    interface Bound<T : GlTexture> : NativeResource {
        val texture: T
        var comparisonMode: TextureComparisonMode
        var comparisonFunc: ComparisonFunc
        var minInterpolation: InterpolationType
        var magInterpolation: InterpolationType
    }
}
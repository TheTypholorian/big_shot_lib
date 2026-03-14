package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlTextureType
import net.typho.big_shot_lib.api.client.opengl.util.*
import org.lwjgl.system.NativeResource

interface GlTexture : GlNamed, NativeResource {
    val format: TextureFormat
    val type: GlTextureType

    fun bind(tracker: GlStateTracker = OpenGL.INSTANCE): Bound<*>

    interface Bound<T : GlTexture> : BoundResource {
        val texture: T
        var compareMode: TextureComparisonMode
        var compareFunc: ComparisonFunc
        var minInterpolation: InterpolationType
        var magInterpolation: InterpolationType
    }
}
package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlTextureType
import net.typho.big_shot_lib.api.client.opengl.util.*
import org.lwjgl.system.NativeResource

interface GlTexture<B : GlTexture.Bound> : GlNamed, NativeResource, GlBindable<B> {
    val format: TextureFormat
    val type: GlTextureType

    override fun bind(tracker: GlStateTracker): B

    interface Bound : BoundResource {
        var compareMode: TextureComparisonMode
        var compareFunc: ComparisonFunc
        var minInterpolation: InterpolationType
        var magInterpolation: InterpolationType
    }
}
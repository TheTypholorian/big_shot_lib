package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.opengl.util.*
import org.lwjgl.system.NativeResource

interface GlTexture : GlBindable, GlNamed, NativeResource {
    fun format(): TextureFormat

    fun type(): TextureType

    fun setCompareMode(mode: TextureComparisonMode) {
        OpenGL.INSTANCE.textureComparisonMode(type(), mode)
    }

    fun setCompareFunc(func: ComparisonFunc) {
        OpenGL.INSTANCE.textureComparisonFunc(type(), func)
    }
}
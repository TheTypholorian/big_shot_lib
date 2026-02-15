package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
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
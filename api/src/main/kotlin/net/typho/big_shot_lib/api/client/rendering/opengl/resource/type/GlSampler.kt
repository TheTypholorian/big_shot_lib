package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureCompareMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureMagFilter
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureMinFilter
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureWrapMode
import org.lwjgl.opengl.GL33.glBindSampler

interface GlSampler : GlResource {
    var compareMode: GlTextureCompareMode
    var compareFunc: GlAlphaFunction

    var minLod: Int
    var maxLod: Int
    var lodBias: Int

    var minFilter: GlTextureMinFilter
    var magFilter: GlTextureMagFilter

    var wrapS: GlTextureWrapMode
    var wrapT: GlTextureWrapMode
    var wrapR: GlTextureWrapMode

    fun bind(unit: Int) {
        glBindSampler(unit, glId)
    }
}
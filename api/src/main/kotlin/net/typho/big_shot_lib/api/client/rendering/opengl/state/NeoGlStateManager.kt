package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.util.EnumArrayMap

interface NeoGlStateManager {
    val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>>
    val program: GlStateStack<Int>
    val programPipeline: GlStateStack<Int>
    val vertexArray: GlStateStack<Int>
    val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>>
    val renderbuffer: GlStateStack<Int>
    val framebuffer: GlStateStack<Int>

    var activeTexture: Int

    companion object {
        @JvmField
        val INSTANCE = NeoGlStateManager::class.loadService()
    }
}
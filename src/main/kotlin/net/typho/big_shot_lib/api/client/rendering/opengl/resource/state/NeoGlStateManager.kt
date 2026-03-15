package net.typho.big_shot_lib.api.client.rendering.opengl.resource.state

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.util.EnumArrayMap

interface NeoGlStateManager {
    val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>>
    val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>>

    companion object {
        @JvmField
        val INSTANCE = NeoGlStateManager::class.loadService()
    }
}
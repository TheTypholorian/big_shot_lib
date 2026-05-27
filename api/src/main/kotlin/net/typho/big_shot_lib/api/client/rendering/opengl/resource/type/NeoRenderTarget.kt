package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import com.mojang.blaze3d.pipeline.RenderTarget
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit
import net.typho.big_shot_lib.api.plugin.Namespace

interface NeoRenderTarget : GlResource {
    @get:Namespace(BigShotApi.MOD_ID)
    @set:Namespace(BigShotApi.MOD_ID)
    var colorTexture: GlTexture2D
    @get:Namespace(BigShotApi.MOD_ID)
    @set:Namespace(BigShotApi.MOD_ID)
    var depthTexture: GlTexture2D?

    fun resize(width: Int, height: Int)

    fun createBuffers(width: Int, height: Int)

    fun bindRead()

    fun unbindRead()

    fun bindWrite(viewport: Boolean)

    fun unbindWrite()

    @Namespace(BigShotApi.MOD_ID)
    fun clear(vararg bits: GlClearBit)

    companion object {
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        operator fun invoke(width: Int, height: Int, depth: Boolean = true, name: () -> String): RenderTarget = InternalUtil.INSTANCE.createRenderTarget(
            width,
            height,
            depth,
            name
        )
    }
}
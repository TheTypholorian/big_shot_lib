package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import com.mojang.blaze3d.pipeline.RenderTarget
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit

interface NeoRenderTarget : GlResource {
    var colorTexture: GlTexture2D
    var depthTexture: GlTexture2D?

    fun resize(width: Int, height: Int)

    fun createBuffers(width: Int, height: Int)

    fun bindRead()

    fun unbindRead()

    fun bindWrite(viewport: Boolean)

    fun unbindWrite()

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
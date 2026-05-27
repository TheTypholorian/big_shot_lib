package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.plugin.Namespace

@Namespace(BigShotApi.MOD_ID)
interface GlResource : GlNamed, AutoCloseable {
    val type: GlResourceType
    val freed: Boolean

    fun checkUsable() {
        if (freed) {
            throw IllegalStateException("Used freed GlResource $this")
        }

        RenderSystem.assertOnRenderThread()
    }

    override fun close()

    interface Container : GlResource
}
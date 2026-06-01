package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed

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
}
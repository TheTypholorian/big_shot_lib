package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.system.NativeResource

interface GlResource : GlNamed, NativeResource {
    val type: GlResourceType
    val freed: Boolean

    fun checkUsable() {
        if (freed) {
            throw IllegalStateException("Used freed GlResource $this")
        }

        RenderSystem.assertOnRenderThread()
    }

    interface Container : GlResource
}
package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import org.lwjgl.system.NativeResource

interface GlResource : GlNamed, NativeResource {
    val type: GlResourceType
    val freed: Boolean
    val context: RenderingContext

    fun checkUsable() {
        if (freed) {
            throw IllegalStateException("Used freed GlResource $this")
        }
    }

    interface Container : GlResource {
        override fun checkUsable() {
            super.checkUsable()

            if (!context.isOnMainThread()) {
                throw IllegalStateException("Tried to use $this on context ${RenderingContext.get()}, even though it's a container object and only valid on ${context.name}")
            }
        }
    }
}
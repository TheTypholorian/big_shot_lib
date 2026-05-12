package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import java.lang.ref.Cleaner

open class NeoGlResource(
    override val type: GlResourceType,
    override val glId: Int,
    val autoFree: Boolean,
    override val context: RenderingContext = RenderingContext.get()
) : GlResource {
    final override var freed = false
        protected set
    private val cleanup: Cleaner.Cleanable = if (autoFree)
        CLEANER.register(this, Cleanup(type, context, glId))
    else
        Cleaner.Cleanable { context.runOrQueue { type.destroy(glId) } }

    override fun free() {
        if (!freed) {
            freed = true
            cleanup.clean()
        }
    }

    override fun toString(): String {
        return "${this::class.simpleName}($glId, autoFree=$autoFree)"
    }

    private class Cleanup(
        val type: GlResourceType,
        val context: RenderingContext,
        val glId: Int
    ) : Runnable {
        override fun run() {
            context.runOrQueue { type.destroy(glId) }
        }
    }

    companion object {
        @JvmStatic
        private val CLEANER = Cleaner.create()
    }
}
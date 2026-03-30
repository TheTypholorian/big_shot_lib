package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import java.lang.ref.Cleaner
import kotlin.reflect.jvm.jvmName

open class NeoGlResource(
    override val type: GlResourceType,
    override val glId: Int,
    val autoFree: Boolean
) : GlResource {
    final override var freed = false
        private set
    private val cleanup: Cleaner.Cleanable = if (autoFree)
        CLEANER.register(this, Cleanup(type, glId))
    else
        Cleaner.Cleanable { GlQueue.INSTANCE.runOrQueue { type.destroy(glId) } }

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
        val glId: Int
    ) : Runnable {
        override fun run() {
            GlQueue.INSTANCE.runOrQueue { type.destroy(glId) }
        }
    }

    companion object {
        @JvmStatic
        private val CLEANER = Cleaner.create()
    }
}
package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.system.NativeResource

interface GlResource : GlNamed, NativeResource {
    val type: GlResourceType
    val freed: Boolean

    interface Container {
        val thread: Thread

        fun existsOnCurrentThread() = thread == Thread.currentThread()

        fun throwIfNotExists() {
            if (!existsOnCurrentThread()) {
                throw IllegalStateException("Tried to use $this on thread ${Thread.currentThread().name}, even though it's a container object and only valid on ${thread.name}")
            }
        }
    }
}
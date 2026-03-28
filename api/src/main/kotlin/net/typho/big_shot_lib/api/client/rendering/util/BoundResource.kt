package net.typho.big_shot_lib.api.client.rendering.util

import org.lwjgl.system.NativeResource

fun interface BoundResource : NativeResource {
    fun unbind()

    override fun free() {
        unbind()
    }
}
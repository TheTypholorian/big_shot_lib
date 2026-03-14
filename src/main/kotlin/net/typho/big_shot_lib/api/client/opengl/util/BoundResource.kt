package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.system.NativeResource

interface BoundResource : NativeResource {
    fun unbind()

    override fun free() = unbind()
}
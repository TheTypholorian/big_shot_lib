package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import org.lwjgl.system.NativeResource

interface GlBoundResource<R : GlResource> : NativeResource {
    val resource: R
    val handle: GlStateStack.Handle<Int>

    fun unbind() {
        handle.pop()
    }

    override fun free() = unbind()
}
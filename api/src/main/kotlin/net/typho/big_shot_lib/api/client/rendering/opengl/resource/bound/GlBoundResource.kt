package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import org.lwjgl.system.NativeResource
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface GlBoundResource<R : GlResource> : NativeResource {
    val resource: R
    val handle: GlStateStack.Handle<Int>

    fun unbind() {
        handle.pop()
    }

    override fun free() = unbind()

    companion object {
        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        inline fun <R> GlBoundResource<*>.assertBound(action: () -> R): R {
            contract {
                callsInPlace(action, InvocationKind.EXACTLY_ONCE)
            }

            assert(handle.isBound) { "Resource $resource was not on top of its gl state stack" }

            return action()
        }
    }
}
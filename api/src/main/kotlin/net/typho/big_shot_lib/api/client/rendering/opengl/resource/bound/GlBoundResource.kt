package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface GlBoundResource<R : GlResource> : BoundResource {
    val resource: R
    val handle: GlStateStack.Handle<Int>

    override fun unbind() {
        handle.pop()
    }

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
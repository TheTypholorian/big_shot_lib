package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.GlBufferResourceType
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface GlBindable {
    fun bind(pushStack: Boolean = true)

    fun unbind(popStack: Boolean = true)

    companion object {
        @JvmStatic
        fun <T> ofStackDynamic(stack: GlStateStack<T>, bound: () -> T) = object : GlBindable {
            override fun bind(pushStack: Boolean) {
                if (pushStack) {
                    stack.push(bound())
                } else {
                    stack.bind(bound())
                }
            }

            override fun unbind(popStack: Boolean) {
                if (popStack) {
                    stack.pop()
                } else {
                    stack.bind(null)
                }
            }
        }

        @JvmStatic
        fun <T> ofStack(stack: GlStateStack<T>, bound: T) = ofStackDynamic(stack) { bound }

        @OptIn(ExperimentalContracts::class)
        inline fun <B : GlBindable, R> B.glUse(touchStack: Boolean = true, crossinline task: (resource: B) -> R): R {
            contract {
                callsInPlace(task, InvocationKind.EXACTLY_ONCE)
            }

            bind(touchStack)
            val r = task(this)
            unbind(touchStack)
            return r
        }

        fun test(): Int {
            val bindable = GlBuffer(GlBufferResourceType.ELEMENT_ARRAY_BUFFER, BufferUsage.DYNAMIC_DRAW)
            return bindable.glUse {
                return@glUse 3
            }
        }
    }
}
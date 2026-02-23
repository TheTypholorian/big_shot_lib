package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import java.util.function.Consumer

interface GlBindable {
    fun bind(pushStack: Boolean = true)

    fun unbind(popStack: Boolean = true)

    companion object {
        @JvmStatic
        fun <T> ofState(setter: Consumer<T>, bound: T, unbound: T) = object : GlBindable {
            override fun bind(pushStack: Boolean) {
                setter.accept(bound)
            }

            override fun unbind(popStack: Boolean) {
                setter.accept(unbound)
            }
        }

        @JvmStatic
        fun <T> ofState(setter: Consumer<T>, bound: T) = object : GlBindable {
            override fun bind(pushStack: Boolean) {
                setter.accept(bound)
            }

            override fun unbind(popStack: Boolean) {
            }
        }

        @JvmStatic
        fun <T> ofStack(stack: GlStateStack<T>, bound: T) = object : GlBindable {
            override fun bind(pushStack: Boolean) {
                if (pushStack) {
                    stack.push(bound)
                } else {
                    stack.bind.accept(bound)
                }
            }

            override fun unbind(popStack: Boolean) {
                if (popStack) {
                    stack.pop()
                } else {
                    stack.bind.accept(null)
                }
            }
        }
    }
}
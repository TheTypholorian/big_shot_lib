package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import java.util.function.Supplier

interface GlBindable {
    fun bind(pushStack: Boolean = true)

    fun unbind(popStack: Boolean = true)

    companion object {
        @JvmStatic
        fun <T> ofStack(stack: GlStateStack<T>, bound: Supplier<T>) = object : GlBindable {
            override fun bind(pushStack: Boolean) {
                if (pushStack) {
                    stack.push(bound.get())
                } else {
                    stack.bind.accept(bound.get())
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

        @JvmStatic
        fun <T> ofStack(stack: GlStateStack<T>, bound: T) = ofStack(stack, Supplier { bound })
    }
}
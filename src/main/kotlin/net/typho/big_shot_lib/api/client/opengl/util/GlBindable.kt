package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments

interface GlBindable : GlAdvancedBindable {
    fun bind(pushStack: Boolean = true)

    override fun bind(arguments: RenderArguments, pushStack: Boolean): GlBindResult {
        bind(pushStack)
        return GlBindResult.Success
    }

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
    }
}
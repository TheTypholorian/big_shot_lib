package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments

interface GlAdvancedBindable {
    fun bind(arguments: RenderArguments = RenderArguments(), pushStack: Boolean = true): GlBindResult

    fun unbind(popStack: Boolean = true)

    companion object {
        @JvmStatic
        fun wrap(bindable: GlBindable) = object : GlAdvancedBindable {
            override fun bind(
                arguments: RenderArguments,
                pushStack: Boolean
            ): GlBindResult {
                bindable.bind(pushStack)
                return GlBindResult.Success
            }

            override fun unbind(popStack: Boolean) {
                bindable.unbind(popStack)
            }
        }
    }
}
package net.typho.big_shot_lib.api.client.rendering.util

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
    }
}
package net.typho.big_shot_lib.api.client.rendering.util

import java.util.function.Consumer

interface GlBindable {
    fun bind()

    fun unbind()

    companion object {
        @JvmStatic
        fun <T> ofState(setter: Consumer<T>, bound: T, unbound: T) = object : GlBindable {
            override fun bind() {
                setter.accept(bound)
            }

            override fun unbind() {
                setter.accept(unbound)
            }
        }

        @JvmStatic
        fun <T> ofState(setter: Consumer<T>, bound: T) = object : GlBindable {
            override fun bind() {
                setter.accept(bound)
            }

            override fun unbind() {
            }
        }
    }
}
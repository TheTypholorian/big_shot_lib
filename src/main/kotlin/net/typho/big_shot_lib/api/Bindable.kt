package net.typho.big_shot_lib.api

import java.util.function.Consumer

interface Bindable {
    fun bind()

    fun unbind()

    companion object {
        fun <T> ofState(setter: Consumer<T>, bound: T, unbound: T) = object : Bindable {
            override fun bind() {
                setter.accept(bound)
            }

            override fun unbind() {
                setter.accept(unbound)
            }
        }
    }
}
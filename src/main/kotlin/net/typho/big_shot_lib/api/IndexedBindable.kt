package net.typho.big_shot_lib.api

import java.util.function.BiConsumer

interface IndexedBindable {
    fun bindBase(index: Int)

    fun unbindBase(index: Int)

    companion object {
        fun <T> ofState(setter: BiConsumer<Int, T>, bound: T, unbound: T) = object : IndexedBindable {
            override fun bindBase(index: Int) {
                setter.accept(index, bound)
            }

            override fun unbindBase(index: Int) {
                setter.accept(index, unbound)
            }
        }

        fun <T> ofState(setter: BiConsumer<Int, T>, bound: T) = object : IndexedBindable {
            override fun bindBase(index: Int) {
                setter.accept(index, bound)
            }

            override fun unbindBase(index: Int) {
            }
        }
    }
}
package net.typho.big_shot_lib.api.util.content

interface ContentFactory<T : Any, K, O, B : ContentFactory.ObjectBuilder<out T>> {
    fun begin(key: K): B

    fun <V : T> beginComplex(key: K): B

    fun end(output: O)

    interface ObjectBuilder<T : Any> {
        fun end(): T
    }
}
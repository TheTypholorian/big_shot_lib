package net.typho.big_shot_lib.api.util.content

interface ContentFactory<T : Any, K, O, B : ObjectBuilder<out T>> {
    fun begin(key: K): B

    fun end(output: O)
}
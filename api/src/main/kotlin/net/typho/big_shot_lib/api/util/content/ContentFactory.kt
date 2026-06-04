package net.typho.big_shot_lib.api.util.content

interface ContentFactory<T : Any, K, O> {
    fun begin(key: K): ObjectBuilder<out T>

    fun end(output: O)
}
package net.typho.big_shot_lib.api.util.content

interface ObjectBuilder<T : Any> {
    fun end(): T
}
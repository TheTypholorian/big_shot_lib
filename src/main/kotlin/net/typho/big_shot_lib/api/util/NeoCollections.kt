package net.typho.big_shot_lib.api.util

object NeoCollections {
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> flatListOf(vararg elements: Any): List<T> {
        return elements.flatMap {
            when (it) {
                is Iterable<*> -> it as? Iterable<T> ?: throw IllegalArgumentException()
                is Iterator<*> -> Iterable { it as? Iterator<T> ?: throw IllegalArgumentException() }
                is T -> listOf(it)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
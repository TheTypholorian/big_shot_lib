package net.typho.big_shot_lib.api.util

interface Extension<T> {
    companion object {
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <V> Extension<V>.cast(): V {
            try {
                return this as V
            } catch (e: ClassCastException) {
                throw ClassCastException("Not allowed to create custom implementations of ${javaClass.name}")
            }
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T, V : T> Extension<T>.castTo(): V {
            return cast() as V
        }
    }
}
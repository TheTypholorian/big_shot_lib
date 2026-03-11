package net.typho.big_shot_lib.api.client.opengl.util

interface GlBinder<V> {
    fun bind(value: V, pushStack: Boolean = true)

    fun unbind(popStack: Boolean = true)

    companion object {
        @JvmStatic
        fun simple(bind: (glId: Int?) -> Unit) = object : GlBinder<Int> {
            override fun bind(value: Int, pushStack: Boolean) {
                bind(value)
            }

            override fun unbind(popStack: Boolean) {
                bind(null)
            }
        }
    }
}
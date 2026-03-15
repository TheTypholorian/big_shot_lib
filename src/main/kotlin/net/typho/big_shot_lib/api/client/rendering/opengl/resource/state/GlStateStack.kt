package net.typho.big_shot_lib.api.client.rendering.opengl.resource.state

interface GlStateStack<V> {
    fun push(value: V): Handle<V>

    fun pop(): V

    interface Handle<V> {
        /**
         * If the top of the stack is equal to this
         */
        val isBound: Boolean
        /**
         * If this is on the top of the stack
         */
        val isTop: Boolean
        /**
         * If this has been popped off the stack
         */
        val isPopped: Boolean
        val stack: GlStateStack<V>
        val value: V

        fun pop()
    }
}
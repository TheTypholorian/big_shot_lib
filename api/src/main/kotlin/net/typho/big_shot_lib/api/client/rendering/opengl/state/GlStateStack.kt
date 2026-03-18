package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.NativeResource

interface GlStateStack<V> {
    fun push(value: V): Handle<V>

    fun pop(): V

    interface Handle<V> : NativeResource {
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

        override fun free() = pop()
    }

    open class Impl<V>(
        @JvmField
        val bind: (value: V) -> Unit,
        @JvmField
        val query: () -> V
    ) : GlStateStack<V> {
        private val list = arrayListOf<HandleImpl>()
        private var restoreTo: V? = null

        override fun push(value: V): Handle<V> {
            if (list.isEmpty()) {
                restoreTo = query()
            }

            val index = list.size
            val handle = HandleImpl(value, index)

            list.add(handle)

            return handle
        }

        override fun pop(): V {
            val handle = list.removeLastOrNull() ?: throw IllegalStateException("Popped empty gl state stack")
            handle.index = -1
            bind()

            if (list.isEmpty()) {
                restoreTo = null
            }

            return handle.value
        }

        private fun bind() {
            (list.lastOrNull()?.value ?: restoreTo)?.let(bind)
        }

        private inner class HandleImpl(
            override val value: V,
            @JvmField
            var index: Int
        ) : Handle<V> {
            override val isBound: Boolean
                get() = list.lastOrNull()?.value == value
            override val isTop: Boolean
                get() = list.lastOrNull() === this
            override val isPopped: Boolean
                get() = index == -1
            override val stack: GlStateStack<V> = this@Impl

            override fun pop() {
                if (index == -1) {
                    throw IllegalStateException("Popped an already popped gl state stack handle")
                }

                if (index == list.size) {
                    pop()
                } else {
                    list.removeAt(index)
                    list.forEachIndexed { i, handle -> handle.index = i }
                }
            }
        }
    }

    open class Flag(
        override val glId: Int
    ) : Impl<Boolean>(
        { if (it) glEnable(glId) else glDisable(glId) },
        { glIsEnabled(glId) }
    ), GlNamed
}
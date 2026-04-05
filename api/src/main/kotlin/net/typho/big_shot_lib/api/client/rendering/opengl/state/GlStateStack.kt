package net.typho.big_shot_lib.api.client.rendering.opengl.state

import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.util.GlFlag
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import org.lwjgl.opengl.GL11.*

interface GlStateStack<V> {
    val size: Int

    fun push(value: V): Handle<V>

    fun pop(): V

    interface Handle<V> : BoundResource {
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

        override fun unbind() = pop()
    }

    open class Impl<V>(
        @JvmField
        val name: String,
        @JvmField
        val bind: (value: V?) -> Unit,
        @JvmField
        val query: () -> V
    ) : GlStateStack<V> {
        private val list = arrayListOf<HandleImpl>()
        private var restoreTo: V? = null
        override val size: Int
            get() = list.size

        override fun push(value: V): Handle<V> {
            RenderSystem.assertOnRenderThread()

            if (list.isEmpty()) {
                restoreTo = query()

                //println("push $name with $value, restore to $restoreTo")
            } else {
                //println("push $name with $value")
            }

            val index = list.size
            val handle = HandleImpl(value, index)

            list.add(handle)
            bind(value)

            return handle
        }

        override fun pop(): V {
            val handle = list.removeLastOrNull() ?: throw IllegalStateException("Popped empty gl state stack")
            handle.index = -1

            if (list.isEmpty()) {
                bind(restoreTo)
                //println("full popped $name to $restoreTo")
            } else {
                val value = list.lastOrNull()?.value
                bind(value)
                //println("popped $name to $value")
            }

            return handle.value
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

                if (index == list.size - 1) {
                    this@Impl.pop()
                } else {
                    //println("popped $name at $index with $list")
                    list.removeAt(index)
                    list.forEachIndexed { i, handle -> handle.index = i }
                }

                index = -1
            }
        }
    }

    open class Flag(
        flag: GlFlag
    ) : Impl<Boolean>(
        flag.name,
        { if (it == true) glEnable(flag.glId) else glDisable(flag.glId) },
        { glIsEnabled(flag.glId) }
    ), GlNamed {
        override val glId: Int = flag.glId
    }
}
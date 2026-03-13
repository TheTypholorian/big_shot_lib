package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL

class GlStateType<V> internal constructor(
    override val glId: Int,
    @JvmField
    val default: V,
    @JvmField
    val name: String,
    @JvmField
    val tracker: GlStateTracker = OpenGL.INSTANCE
) : GlNamed {
    private val stack = arrayListOf<V>()
    private var restoreTo: V? = null

    fun push(value: V) {
        if (stack.isEmpty()) {
            restoreTo = tracker[this]
        }

        if (stack.lastOrNull() != value) {
            tracker[this] = value
        }

        stack.add(value)
    }

    fun pop() {
        val removed = stack.removeLastOrNull() ?: throw IllegalStateException("Popped empty gl state stack $name")
        val current = currentValue()

        if (current == null) {
            tracker[this] = restoreTo ?: default
        } else if (current != removed) {
            tracker[this] = current
        }
    }

    fun currentValue(): V? = stack.lastOrNull()
}
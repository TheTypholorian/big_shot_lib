package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed

sealed class GlStateType<V>(
    override val glId: Int,
    @JvmField
    val default: V
) : GlNamed {
    protected val stack = arrayListOf<V>()
    protected var restoreTo: V? = null
    abstract val name: String

    abstract fun rawBind(value: V)

    abstract fun rawQueryValue(): V
    
    fun push(value: V) {
        if (stack.isEmpty()) {
            restoreTo = rawQueryValue()
        }

        if (stack.lastOrNull() != value) {
            rawBind(value)
        }

        stack.add(value)
    }

    fun pop() {
        val removed = stack.removeLastOrNull() ?: throw IllegalStateException("Popped empty gl state stack $name")
        val current = currentValue()

        if (current == null) {
            rawBind(restoreTo ?: default)
        } else if (current != removed) {
            rawBind(current)
        }
    }

    fun currentValue(): V? = stack.lastOrNull()
}
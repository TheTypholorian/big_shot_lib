package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.buffers.Mesh
import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.system.NativeResource

open class GlWhiteboard : NativeResource {
    @JvmField
    protected val touched = HashSet<GlStateStack<*>>()
    @JvmField
    protected val values = HashMap<GlStateStack<*>, () -> Unit>()

    fun <V> bind(stack: GlStateStack<V>, value: V) {
        values[stack] = { stack.bind(value, false) }
        touched.add(stack)
    }

    fun bind(stack: GlStateStack<Int>, resource: GlNamed) {
        bind(stack, resource.glId)
    }

    fun bind(buffer: GlBuffer) {
        bind(buffer.binder)
    }

    fun flush() {
        values.forEach { (stack, function) -> function() }
        values.clear()
    }

    fun draw(mesh: Mesh) {
        flush()
        mesh.draw()
    }

    override fun free() {
        touched.forEach { it.rebind() }
    }
}
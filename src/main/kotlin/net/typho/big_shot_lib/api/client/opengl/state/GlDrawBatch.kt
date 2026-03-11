package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.buffers.Mesh
import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.system.NativeResource

open class GlDrawBatch : NativeResource {
    @JvmField
    protected val touched = HashSet<GlStateManager<*>>()
    @JvmField
    protected val values = HashMap<GlStateManager<*>, () -> Unit>()
    var arrayBuffer: GlBuffer
        set(value) {
            field = value
            set(GlStateManager.buffers[BufferType.])
        }

    fun <V> set(stack: GlStateManager<V>, value: V) {
        values[stack] = { stack.bind(value, false) }
        touched.add(stack)
    }

    fun set(stack: GlStateManager<Int>, resource: GlNamed) {
        set(stack, resource.glId)
    }

    fun set(buffer: GlBuffer) {
        set()
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
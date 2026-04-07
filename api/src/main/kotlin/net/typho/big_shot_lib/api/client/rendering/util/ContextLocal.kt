package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import org.lwjgl.glfw.GLFW
import org.lwjgl.system.NativeResource

class ContextLocal<R : GlResource.Container> : NativeResource {
    val resources = hashMapOf<Long, R>()

    fun get() = resources[GLFW.glfwGetCurrentContext()]

    fun set(resource: R, freeOld: Boolean = true) {
        if (freeOld) {
            resources.put(GLFW.glfwGetCurrentContext(), resource)?.free()
        } else {
            resources[GLFW.glfwGetCurrentContext()] = resource
        }
    }

    fun getOrSet(value: () -> R): R {
        return resources.computeIfAbsent(GLFW.glfwGetCurrentContext()) { value() }
    }

    override fun free() {
        resources.forEach { (context, resource) -> resource.free() }
        resources.clear()
    }
}

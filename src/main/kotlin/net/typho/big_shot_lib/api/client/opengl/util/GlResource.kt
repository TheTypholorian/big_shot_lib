package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import org.lwjgl.system.NativeResource

abstract class GlResource<T : GlResourceType>(
    @JvmField
    val type: T,
    override val glId: Int = type.create()
) : GlNamed, GlBindable, NativeResource {
    override fun bind(pushStack: Boolean) {
        type.bind(glId, pushStack)
    }

    override fun unbind(popStack: Boolean) {
        type.unbind(popStack)
    }

    override fun toString(): String {
        return "${type.name}($glId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GlResource<*>) return false

        if (glId != other.glId) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return glId
    }
}
package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.system.NativeResource

abstract class GlResource(
    override val glId: Int,
    @JvmField
    val binder: GlBinder<Int>
) : GlNamed, GlBindable, NativeResource {
    override fun bind(pushStack: Boolean) {
        binder.bind(glId, pushStack)
    }

    override fun unbind(popStack: Boolean) {
        binder.unbind(popStack)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}($glId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GlResource

        return glId == other.glId
    }

    override fun hashCode(): Int {
        return glId
    }
}
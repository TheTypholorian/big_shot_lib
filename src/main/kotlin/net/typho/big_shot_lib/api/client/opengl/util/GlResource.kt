package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import org.lwjgl.system.NativeResource

abstract class GlResource(
    override val glId: Int,
    @JvmField
    val stack: GlStateStack<Int>
) : GlNamed, GlBindable, NativeResource {
    override fun bind(pushStack: Boolean) {
        if (pushStack) {
            stack.push(glId)
        } else {
            stack.bind.accept(glId)
        }
    }

    override fun unbind(popStack: Boolean) {
        if (popStack) {
            stack.pop()
        } else {
            stack.bind.accept(0)
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId)"
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
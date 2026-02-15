package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack
import org.lwjgl.system.NativeResource

abstract class GlResource(
    @JvmField
    val glId: Int,
    @JvmField
    val stack: GlStateStack
) : GlNamed, GlBindable, NativeResource {
    final override fun glId() = glId

    override fun bind() = stack.push(this)

    override fun unbind() = stack.pop()

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
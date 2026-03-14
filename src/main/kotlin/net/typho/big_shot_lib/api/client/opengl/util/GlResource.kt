package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import org.lwjgl.system.NativeResource

abstract class GlResource(
    @JvmField
    val resourceType: GlResourceType,
    override val glId: Int = resourceType.create()
) : GlNamed, NativeResource {
    override fun free() {
        resourceType.delete(glId)
    }

    override fun toString(): String {
        return "${resourceType.name}($glId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GlResource) return false

        if (glId != other.glId) return false
        if (resourceType != other.resourceType) return false

        return true
    }

    override fun hashCode(): Int {
        return glId
    }
}
package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResource
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType

open class NeoGlResource(
    override val type: GlResourceType,
    override val glId: Int
) : GlResource {
    final override var freed = false
        protected set

    override fun free() {
        if (!freed) {
            freed = true
            GlQueue.INSTANCE.runOrQueue { type.destroy(glId) }
        }
    }

    override fun toString(): String {
        return "${this::class.simpleName}($glId)"
    }
}
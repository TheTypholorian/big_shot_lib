package net.typho.big_shot_lib.api.buffers

import net.typho.big_shot_lib.api.GlResource
import net.typho.big_shot_lib.api.StateManager
import net.typho.big_shot_lib.api.textures.TextureFormat

open class GlRenderBuffer(
    glId: Int,
    @JvmField
    val format: TextureFormat
) : GlResource(glId) {
    constructor(format: TextureFormat) : this(StateManager.INSTANCE.createRenderBuffer(), format)

    override fun bind(glId: Int) {
        StateManager.INSTANCE.bindRenderBuffer(glId)
    }

    override fun free() {
        StateManager.INSTANCE.deleteRenderBuffer(glId)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}
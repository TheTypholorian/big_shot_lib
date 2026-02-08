package net.typho.big_shot_lib.api.buffers

import net.typho.big_shot_lib.api.GlResource
import net.typho.big_shot_lib.api.OpenGL
import net.typho.big_shot_lib.api.textures.TextureFormat

open class GlRenderBuffer(
    glId: Int,
    @JvmField
    val format: TextureFormat
) : GlResource(glId) {
    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createRenderBuffer(), format)

    override fun bind(glId: Int) {
        OpenGL.INSTANCE.bindRenderBuffer(glId)
    }

    override fun free() {
        OpenGL.INSTANCE.deleteRenderBuffer(glId)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}
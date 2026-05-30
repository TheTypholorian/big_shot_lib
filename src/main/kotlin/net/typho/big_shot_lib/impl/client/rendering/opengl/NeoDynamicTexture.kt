package net.typho.big_shot_lib.impl.client.rendering.opengl

import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType

open class NeoDynamicTexture(
    override val width: Int,
    override val height: Int,
    override val format: GlTextureFormat,
    blur: Boolean,
    mipmap: Boolean,
    override val glId: Int
): AbstractTexture() {
    override val type: GlResourceType
        get() = GlResourceType.TEXTURE
    final override var freed: Boolean = false
        private set

    override var blur: Boolean
        get() = super.blur
        set(value) {
            super.blur = value
        }
    override var mipmap: Boolean
        get() = super.mipmap
        set(value) {
            super.mipmap = value
        }

    init {
        setFilter(blur, mipmap)
    }

    override fun load(resourceManager: ResourceManager) {
    }
}
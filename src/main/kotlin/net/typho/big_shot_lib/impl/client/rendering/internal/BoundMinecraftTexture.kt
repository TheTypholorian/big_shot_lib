package net.typho.big_shot_lib.impl.client.rendering.internal

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack

internal class BoundMinecraftTexture(
    resource: GlTexture2D,
    target: GlTextureTarget,
    handle: GlStateStack.Handle<Int>,
    @JvmField
    val width: Int,
    @JvmField
    val height: Int,
    @JvmField
    val format: GlTextureFormat? = null
) : GlBoundTexture2D.Basic(resource, target, handle) {
    override fun resize(width: Int, height: Int, format: GlTextureFormat) {
        if (width != this.width || height != this.height) {
            throw UnsupportedOperationException("Cannot resize a Minecraft GlTexture from " + this.width + "x" + this.height + " to " + width + "x" + height)
        }

        if (format != this.format) {
            throw UnsupportedOperationException("Cannot change a Minecraft GlTexture's format from " + this.format + " to " + format)
        }
    }
}
package net.typho.big_shot_lib.api.builtin

import net.minecraft.client.renderer.texture.AbstractTexture
import net.typho.big_shot_lib.api.ITexture
import net.typho.big_shot_lib.gl.InterpolationType
import net.typho.big_shot_lib.gl.resource.GlResourceType
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

abstract class AbstractBuiltinTexture<T : AbstractTexture>(val inner: T) : ITexture {
    override fun setInterpolation(interpolation: InterpolationType) {
        inner.setFilter(interpolation == InterpolationType.LINEAR, true)
    }

    override fun type() = GlResourceType.TEXTURE_2D

    override fun release() {
        inner.close()
    }

    override fun id() = inner.id

    override fun attach2D(attachment: Int, target: Int) {
        glFramebufferTexture2D(
            target,
            attachment,
            type().glName,
            id(),
            0
        )
    }

    override fun resize2D(width: Int, height: Int) {
        throw UnsupportedOperationException("Builtin minecraft textures aren't resizable, only big shot textures are")
    }
}
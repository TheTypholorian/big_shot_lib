package net.typho.big_shot_lib.api.impl

import net.minecraft.client.renderer.texture.AbstractTexture
import net.typho.big_shot_lib.api.ITexture
import net.typho.big_shot_lib.gl.Unbindable
import net.typho.big_shot_lib.gl.resource.GlResourceType
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

abstract class AbstractBuiltinTexture<T : AbstractTexture>(val inner: T) : ITexture {
    override fun type() = GlResourceType.TEXTURE_2D

    override fun bind(): Unbindable<AbstractBuiltinTexture<T>> {
        inner.bind()
        return Unbindable.of(this)
    }

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
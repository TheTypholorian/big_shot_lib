package net.typho.big_shot_lib.textures

import net.minecraft.client.renderer.texture.AbstractTexture
import net.typho.big_shot_lib.gl.resource.GlResourceType
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

abstract class AbstractBuiltinTexture<T : AbstractTexture>(val inner: T) : ITexture {
    override fun getWidth(): Int {
        bind()
        val width = glGetTexLevelParameteri(type().glName, 0, GL_TEXTURE_WIDTH)
        unbind()
        return width
    }

    override fun getHeight(): Int {
        bind()
        val height = glGetTexLevelParameteri(type().glName, 0, GL_TEXTURE_HEIGHT)
        unbind()
        return height
    }

    override fun getMinInterpolation() = InterpolationType.from(inner.blur)

    override fun getMagInterpolation() = InterpolationType.from(inner.blur)

    override fun getSWrapping(): WrappingType {
        bind()
        val wrap = glGetTexLevelParameteri(type().glName, 0, GL_TEXTURE_WRAP_S)
        unbind()
        return WrappingType.from(wrap)!!
    }

    override fun getTWrapping(): WrappingType {
        bind()
        val wrap = glGetTexLevelParameteri(type().glName, 0, GL_TEXTURE_WRAP_T)
        unbind()
        return WrappingType.from(wrap)!!
    }

    override fun type() = GlResourceType.TEXTURE_2D

    override fun release() {
        inner.close()
    }

    override fun id() = inner.id

    override fun attachToFramebuffer(attachment: Int, target: Int) {
        glFramebufferTexture2D(
            target,
            attachment,
            type().glName,
            id(),
            0
        )
    }

    override fun resize(width: Int, height: Int) {
        throw UnsupportedOperationException("Builtin minecraft textures aren't resizable, only big shot textures are")
    }
}
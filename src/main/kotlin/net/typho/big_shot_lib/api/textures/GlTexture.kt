package net.typho.big_shot_lib.api.textures

import net.typho.big_shot_lib.api.BufferUploader
import net.typho.big_shot_lib.api.GlResource
import net.typho.big_shot_lib.api.StateManager
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import java.nio.ByteBuffer

open class GlTexture(
    glId: Int,
    @JvmField
    val format: TextureFormat
) : GlResource(glId) {
    constructor(format: TextureFormat) : this(StateManager.INSTANCE.createTexture(), format)

    override fun bind(glId: Int) = StateManager.INSTANCE.bindTexture(GL_TEXTURE_2D, glId)

    override fun free() {
        StateManager.INSTANCE.deleteTexture(glId)
    }

    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        StateManager.INSTANCE.textureInterpolation(GL_TEXTURE_2D, min, mag, glId)
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s) {
        StateManager.INSTANCE.textureWrapping(GL_TEXTURE_2D, s, t, glId)
    }

    fun size(width: Int, height: Int): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                StateManager.INSTANCE.textureData2D(format, width, height, buffer)
            }
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}
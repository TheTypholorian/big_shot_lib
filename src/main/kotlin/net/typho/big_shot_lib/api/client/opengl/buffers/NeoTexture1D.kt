package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_FUNC
import org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_MODE
import java.nio.ByteBuffer

open class NeoTexture1D(
    override val format: TextureFormat,
    glId: Int = GlResourceType.TEXTURE.create()
) : GlResource(GlResourceType.TEXTURE, glId), GlTexture1D {
    companion object {
        @JvmField
        val NULL = NeoTexture1D(TextureFormat.NULL, 0)
    }

    override fun bind(tracker: GlStateTracker): GlTexture1D.Bound {
        type.state.push(glId, tracker)

        return object : GlTexture1D.Bound {
            override var sWrapping: WrappingType
                get() = GlNamed.glIdToEnum<WrappingType>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_WRAP_S))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_WRAP_S, value.glId)
                }
            override var compareMode: TextureComparisonMode
                get() = GlNamed.glIdToEnum<TextureComparisonMode>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_COMPARE_MODE))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_COMPARE_MODE, value.glId)
                }
            override var compareFunc: ComparisonFunc
                get() = GlNamed.glIdToEnum<ComparisonFunc>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_COMPARE_FUNC))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_COMPARE_FUNC, value.glId)
                }
            override var minInterpolation: InterpolationType
                get() = GlNamed.glIdToEnum<InterpolationType>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_MIN_FILTER))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_MIN_FILTER, value.glId)
                }
            override var magInterpolation: InterpolationType
                get() = GlNamed.glIdToEnum<InterpolationType>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_MAG_FILTER))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_MAG_FILTER, value.glId)
                }

            override fun resize(
                width: Int,
                upload: (uploader: BufferUploader) -> Unit
            ) {
                upload(object : BufferUploader {
                    override fun upload(buffer: ByteBuffer) {
                        OpenGL.INSTANCE.textureData1D(type.glId, format, width, buffer)
                    }

                    override fun uploadNull() {
                        OpenGL.INSTANCE.textureData1D(type.glId, format, width, 0L)
                    }
                })
            }

            override fun unbind() {
                type.state.pop(tracker)
            }
        }
    }

    override fun toString(): String {
        return "${resourceType.name}(glId=$glId, format=$format)"
    }
}
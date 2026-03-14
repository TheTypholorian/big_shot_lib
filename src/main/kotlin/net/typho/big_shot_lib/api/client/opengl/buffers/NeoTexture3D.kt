package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.ComparisonFunc
import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R
import org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_FUNC
import org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_MODE
import java.nio.ByteBuffer

open class NeoTexture3D(
    override val format: TextureFormat,
    glId: Int = GlResourceType.TEXTURE.create()
) : GlResource(GlResourceType.TEXTURE, glId), GlTexture3D {
    companion object {
        @JvmField
        val NULL = NeoTexture3D(TextureFormat.NULL, 0)
    }

    override fun bind(tracker: GlStateTracker): GlTexture3D.Bound<*> {
        type.state.push(glId, tracker)

        return object : GlTexture3D.Bound<NeoTexture3D> {
            override val texture = this@NeoTexture3D
            override var sWrapping: WrappingType
                get() = GlNamed.glIdToEnum<WrappingType>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_WRAP_S))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_WRAP_S, value.glId)
                }
            override var tWrapping: WrappingType
                get() = GlNamed.glIdToEnum<WrappingType>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_WRAP_T))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_WRAP_T, value.glId)
                }
            override var rWrapping: WrappingType
                get() = GlNamed.glIdToEnum<WrappingType>(OpenGL.INSTANCE.getTextureParameter(type.glId, GL_TEXTURE_WRAP_R))
                set(value) {
                    OpenGL.INSTANCE.textureParameter(type.glId, GL_TEXTURE_WRAP_R, value.glId)
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
                height: Int,
                depth: Int,
                upload: (uploader: BufferUploader) -> Unit
            ) {
                upload(object : BufferUploader {
                    override fun upload(buffer: ByteBuffer) {
                        OpenGL.INSTANCE.textureData3D(type.glId, format, width, height, depth, buffer)
                    }

                    override fun uploadNull() {
                        OpenGL.INSTANCE.textureData3D(type.glId, format, width, height, depth, 0L)
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
package net.typho.big_shot_lib.api.client.util.dynamic_buffers

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoTexture2D
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import java.util.function.Consumer

object AlbedoDynamicBuffer : DynamicBuffer<AlbedoDynamicBuffer.MixinInstance> {
    private var location: Int? = null
    val texture by lazy {
        NeoTexture2D(format())
    }
    const val VERTEX_TEX_COORD_VAR_NAME = "BigShotVertexTexCoord"
    const val VERTEX_COLOR_VAR_NAME = "BigShotVertexColor"
    const val FRAGMENT_VAR_NAME = "BigShotFragmentAlbedo"
    private val impl = Impl::class.loadService()

    override fun location(): ResourceIdentifier {
        return BigShotApi.id("dynamic_buffer/albedo")
    }

    override fun blend(): Boolean {
        return false
    }

    override fun format(): TextureFormat {
        return TextureFormat.RGBA8
    }

    override fun attachToFramebuffer(attachment: Int) {
        texture.attachToFramebuffer(attachment)
        location = attachment - GL_COLOR_ATTACHMENT0
    }

    override fun setShaderLocation(location: Int) {
        AlbedoDynamicBuffer.location = location
    }

    override fun resize(
        width: Int,
        height: Int,
        upload: Consumer<BufferUploader>
    ) {
        texture.resize(width, height, upload)
    }

    override fun create(
        key: ShaderProgramKey,
        parent: ShaderMixinManager.Instance
    ): MixinInstance? {
        return impl.create(key, parent)
    }

    interface MixinInstance : ShaderMixin {
        val fragLocation: Int
    }

    interface Impl {
        fun create(key: ShaderProgramKey, parent: ShaderMixinManager.Instance): MixinInstance?
    }
}
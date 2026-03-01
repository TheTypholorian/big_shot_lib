package net.typho.big_shot_lib.api.client.util.dynamic_buffers

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoTexture2D
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.util.function.Consumer

object AlbedoDynamicBuffer : DynamicBuffer<AlbedoDynamicBuffer.MixinInstance> {
    const val VERTEX_TEX_COORD_VAR_NAME = "BigShotVertexTexCoord"
    const val VERTEX_COLOR_VAR_NAME = "BigShotVertexColor"
    const val FRAGMENT_VAR_NAME = "BigShotFragmentAlbedo"

    private val impl = Impl::class.loadService()
    override val location = BigShotApi.id("dynamic_buffer/albedo")
    override val format = TextureFormat.RGBA8
    override val blend = true
    override var shaderLocation: Int? = null

    val texture by lazy {
        NeoTexture2D(format)
    }

    override fun attachToFramebuffer(attachment: Int) {
        texture.attachToFramebuffer(attachment)
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
        return impl.create(key, parent, shaderLocation)
    }

    interface MixinInstance : ShaderMixin {
        val fragLocation: Int
    }

    interface Impl {
        fun create(key: ShaderProgramKey, parent: ShaderMixinManager.Instance, location: Int?): MixinInstance?
    }
}
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

object NormalsDynamicBuffer : DynamicBuffer<NormalsDynamicBuffer.MixinInstance> {
    const val INPUT_VAR_NAME = "a_Normal"
    const val VERTEX_VAR_NAME = "BigShotVertexNormal"
    const val FRAGMENT_VAR_NAME = "BigShotFragmentNormal"

    private val impl = Impl::class.loadService()
    override val location = BigShotApi.id("dynamic_buffer/normals")
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
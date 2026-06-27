package net.typho.big_shot_lib.api.client.rendering.common

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBufferUsage
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureUsage
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat
import net.typho.big_shot_lib.api.client.rendering.state.GpuDrawSettings
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

private val INSTANCE by lazy { IGpuObjects::class.loadService() }

object GpuObjects : IGpuObjects by INSTANCE {
    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: GpuTextureUsage
    ): GpuTexture {
        return texture(name, width, height, usage, GpuTextureFormat.RGBA8)
    }

    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: GpuTextureUsage,
        format: GpuTextureFormat
    ): GpuTexture {
        return texture(name, width, height, usage, format, false, false)
    }
}

interface IGpuObjects {
    fun renderType(
        location: Identifier,
        format: VertexFormat,
        drawState: GpuDrawSettings.Builder,
        defaultBufferSize: Int,
        affectsCrumbling: Boolean,
        sortOnUpload: Boolean,
        isOutline: Boolean
    ): RenderType

    /*
    fun framebuffer(
        width: Int,
        height: Int,
        useDepth: Boolean,
        name: () -> String
    ): GpuFramebuffer

    fun framebuffer(
        color: GpuTexture?,
        depth: GpuTexture?,
        name: () -> String
    ): GpuFramebuffer
     */

    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: GpuTextureUsage,
        format: GpuTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): GpuTexture

    fun buffer(
        name: GpuObjectName?,
        size: Long,
        usage: GpuBufferUsage
    ): GpuBuffer
}
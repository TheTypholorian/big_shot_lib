package net.typho.big_shot_lib.client.api.rendering.common

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuBufferUsage
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuTextureUsage
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuTextureFormat
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.util.buffer.MemoryPointer
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter
import java.util.function.Consumer

private val INSTANCE by lazy { IGpuObjects::class.loadService() }

object GpuObjects : IGpuObjects by INSTANCE {
    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: GpuTextureUsage
    ): GpuTexture {
        return texture(name, width, height, usage, GpuTextureFormat.rgba)
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

    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: Int,
        format: GpuTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): GpuTexture {
        return texture(name, width, height, GpuTextureUsage(usage), format, blur, mipmap)
    }

    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: Int
    ): GpuTexture {
        return texture(name, width, height, usage, GpuTextureFormat.rgba)
    }

    fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: Int,
        format: GpuTextureFormat
    ): GpuTexture {
        return texture(name, width, height, usage, format, false, false)
    }

    fun buffer(
        name: GpuObjectName?,
        size: Long,
        usage: Int
    ): GpuBuffer {
        return buffer(name, size, GpuBufferUsage(usage))
    }

    fun buffer(
        name: GpuObjectName?,
        usage: Int,
        data: MemoryPointer
    ): GpuBuffer {
        return buffer(name, GpuBufferUsage(usage), data)
    }

    fun buffer(
        name: GpuObjectName?,
        size: Long,
        usage: GpuBufferUsage,
        data: Consumer<MemoryWriter>
    ): GpuBuffer {
        MemoryPointer.alloc(size).use {
            data.accept(it.write())
            return buffer(name, usage, it)
        }
    }

    fun buffer(
        name: GpuObjectName?,
        size: Long,
        usage: Int,
        data: Consumer<MemoryWriter>
    ): GpuBuffer {
        return buffer(name, size, GpuBufferUsage(usage), data)
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

    fun buffer(
        name: GpuObjectName?,
        usage: GpuBufferUsage,
        data: MemoryPointer
    ): GpuBuffer
}
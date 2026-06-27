package net.typho.big_shot_lib.api.client.rendering.common

import com.mojang.blaze3d.platform.NativeImage
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat
import net.typho.big_shot_lib.api.util.Extension
import org.joml.Vector4fc
import java.nio.ByteBuffer

interface GpuTexture : Extension<GpuTexture>, GpuResource {
    val width: Int
    val height: Int
    val format: GpuTextureFormat
    override val type: GpuResourceType
        get() = GpuResourceType.TEXTURE

    fun clear(color: Vector4fc)

    fun upload(
        data: NativeImage,
        mipLevel: Int,
        depthOrLayer: Int,
        destX: Int,
        destY: Int
    )

    fun upload(
        data: NativeImage,
        mipLevel: Int,
        depthOrLayer: Int,
        destX: Int,
        destY: Int,
        width: Int,
        height: Int
    )

    fun upload(
        data: ByteBuffer,
        mipLevel: Int,
        depthOrLayer: Int,
        destX: Int,
        destY: Int,
        width: Int,
        height: Int
    )
}
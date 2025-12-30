package net.typho.big_shot_lib.gl.resource

import com.mojang.blaze3d.platform.NativeImage
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.*
import org.lwjgl.opengl.GL41.GL_RGB10_A2UI
import org.lwjgl.opengl.GL41.GL_RGB565

/**
 * @author Developers of Veil
 */
enum class TextureFormat(
    val id: Int,
    val internal: Int,
    val type: Int = GL_UNSIGNED_BYTE,
    val color: Boolean = false,
    val depth: Boolean = false,
    val stencil: Boolean = false
) {
    RED(GL_RED, GL_RED, color = true),
    RG(GL_RG, GL_RG, color = true),
    RGB(GL_RGB, GL_RGB, color = true),
    BGR(GL_BGR, GL_BGR, color = true),
    RGBA(GL_RGBA, GL_RGBA, color = true),
    BGRA(GL_BGRA, GL_BGRA, color = true),
    R8(GL_RED, GL_R8, color = true),
    R8_SNORM(GL_RED, GL_R8_SNORM, color = true),
    R16(GL_RED, GL_R16, color = true),
    R16_SNORM(GL_RED, GL_R16_SNORM, color = true),
    RG8(GL_RG, GL_RG8, color = true),
    RG8_SNORM(GL_RG, GL_RG8_SNORM, color = true),
    RG16(GL_RG, GL_RG16, color = true),
    RG16_SNORM(GL_RG, GL_RG16_SNORM, color = true),
    R3_G3_B2(GL_RGB, GL_R3_G3_B2, color = true),
    RGB4(GL_RGB, GL_RGB4, color = true),
    RGB5(GL_RGB, GL_RGB5, color = true),
    RGB565(GL_RGB, GL_RGB565, color = true),
    RGB8(GL_RGB, GL_RGB8, color = true),
    RGB8_SNORM(GL_RGB, GL_RGB8_SNORM, color = true),
    RGB10(GL_RGB, GL_RGB10, color = true),
    RGB12(GL_RGB, GL_RGB12, color = true),
    RGB16(GL_RGB, GL_RGB16, color = true),
    RGB16_SNORM(GL_RGB, GL_RGB16_SNORM, color = true),
    RGBA2(GL_RGBA, GL_RGBA2, color = true),
    RGBA4(GL_RGBA, GL_RGBA4, color = true),
    RGB5_A1(GL_RGBA, GL_RGB5_A1, color = true),
    RGBA8(GL_RGBA, GL_RGBA8, color = true),
    RGBA8_SNORM(GL_RGBA, GL_RGBA8_SNORM, color = true),
    RGB10_A2(GL_RGBA, GL_RGB10_A2, color = true),
    RGB10_A2UI(GL_RGBA_INTEGER, GL_RGB10_A2UI, color = true),
    RGBA12(GL_RGBA, GL_RGBA12, color = true),
    RGBA16(GL_RGBA, GL_RGBA16, color = true),
    RGBA16_SNORM(GL_RGBA, GL_RGBA16_SNORM, color = true),
    SRGB(GL_RGB, GL_SRGB, color = true),
    SRGB8(GL_RGB, GL_SRGB8, color = true),
    SRGB_ALPHA(GL_RGBA, GL_SRGB_ALPHA, color = true),
    SRGB8_ALPHA8(GL_RGBA, GL_SRGB8_ALPHA8, color = true),
    COMPRESSED_SRGB(GL_RGB, GL_COMPRESSED_SRGB, color = true),
    COMPRESSED_SRGB_ALPHA(GL_RGBA, GL_COMPRESSED_SRGB_ALPHA, color = true),
    R16F(GL_RED, GL_R16F, color = true),
    RG16F(GL_RG, GL_RG16F, color = true),
    RGB16F(GL_RGB, GL_RGB16F, color = true),
    RGBA16F(GL_RGBA, GL_RGBA16F, color = true),
    R32F(GL_RED, GL_R32F, color = true),
    RG32F(GL_RG, GL_R32F, color = true),
    RGB32F(GL_RGB, GL_RGB32F, color = true),
    RGB9_E5(GL_RGB, GL_RGB9_E5, color = true),
    RGBA32F(GL_RGBA, GL_RGBA32F, color = true),
    R11F_G11F_B10F(GL_RGBA, GL_R11F_G11F_B10F, color = true),
    R8I(GL_RED_INTEGER, GL_R8I, color = true),
    R8UI(GL_RED_INTEGER, GL_R8UI, color = true),
    R16I(GL_RED_INTEGER, GL_R16I, color = true),
    R16UI(GL_RED_INTEGER, GL_R16UI, color = true),
    R32I(GL_RED_INTEGER, GL_R32I, color = true),
    R32UI(GL_RED_INTEGER, GL_R32UI, color = true),
    RG8I(GL_RG_INTEGER, GL_RG8I, color = true),
    RG8UI(GL_RG_INTEGER, GL_RG8UI, color = true),
    RG16I(GL_RG_INTEGER, GL_RG16I, color = true),
    RG16UI(GL_RG_INTEGER, GL_RG16UI, color = true),
    RG32I(GL_RG_INTEGER, GL_RG32I, color = true),
    RG32UI(GL_RG_INTEGER, GL_RG32UI, color = true),
    RGB8I(GL_RGB_INTEGER, GL_RGB8I, color = true),
    RGB8UI(GL_RGB_INTEGER, GL_RGB8UI, color = true),
    RGB16I(GL_RGB_INTEGER, GL_RGB16I, color = true),
    RGB16UI(GL_RGB_INTEGER, GL_RGB16UI, color = true),
    RGB32I(GL_RGB_INTEGER, GL_RGB32I, color = true),
    RGB32UI(GL_RGB_INTEGER, GL_RGB32UI, color = true),
    RGBA8I(GL_RGBA_INTEGER, GL_RGBA8I, color = true),
    RGBA8UI(GL_RGBA_INTEGER, GL_RGBA8UI, color = true),
    RGBA16I(GL_RGBA_INTEGER, GL_RGBA16I, color = true),
    RGBA16UI(GL_RGBA_INTEGER, GL_RGBA16UI, color = true),
    RGBA32I(GL_RGBA_INTEGER, GL_RGBA32I, color = true),
    RGBA32UI(GL_RGBA_INTEGER, GL_RGBA32UI, color = true),

    DEPTH_COMPONENT(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_FLOAT, depth = true),
    DEPTH_STENCIL(GL_DEPTH_STENCIL, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, depth = true, stencil = true),
    DEPTH_COMPONENT16(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT16, GL_FLOAT, depth = true),
    DEPTH_COMPONENT24(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT24, GL_FLOAT, depth = true),
    DEPTH_COMPONENT32(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT32, GL_FLOAT, depth = true),
    DEPTH_COMPONENT32F(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT32F, GL_FLOAT, depth = true),
    DEPTH24_STENCIL8(GL_DEPTH_STENCIL, GL_DEPTH24_STENCIL8, GL_UNSIGNED_INT_24_8, depth = true, stencil = true),
    DEPTH32F_STENCIL8(GL_DEPTH_STENCIL, GL_DEPTH32F_STENCIL8, GL_FLOAT_32_UNSIGNED_INT_24_8_REV, depth = true, stencil = true);

    companion object {
        fun get(format: NativeImage.Format): TextureFormat = when (format) {
            NativeImage.Format.RGBA -> RGBA
            NativeImage.Format.RGB -> RGB
            NativeImage.Format.LUMINANCE_ALPHA -> RG
            NativeImage.Format.LUMINANCE -> RED
        }
    }
}
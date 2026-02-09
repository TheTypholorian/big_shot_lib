package net.typho.big_shot_lib.api.textures

import net.typho.big_shot_lib.api.util.GlNamed
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_BGR
import org.lwjgl.opengl.GL12.GL_BGRA
import org.lwjgl.opengl.GL14.*
import org.lwjgl.opengl.GL21.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.*
import org.lwjgl.opengl.GL33.GL_RGB10_A2UI
import org.lwjgl.opengl.GL41.GL_RGB565

enum class TextureFormat(
    @JvmField
    val glId: Int,
    @JvmField
    val internalId: Int,
    @JvmField
    val type: Int = GL_UNSIGNED_BYTE,
    @JvmField
    val hasColor: Boolean = false,
    @JvmField
    val hasDepth: Boolean = false,
    @JvmField
    val hasStencil: Boolean = false
) : GlNamed {
    NULL(0, 0),

    RED(GL_RED, GL_RED, hasColor = true),
    RG(GL_RG, GL_RG, hasColor = true),
    RGB(GL_RGB, GL_RGB, hasColor = true),
    BGR(GL_BGR, GL_BGR, hasColor = true),
    RGBA(GL_RGBA, GL_RGBA, hasColor = true),
    BGRA(GL_BGRA, GL_BGRA, hasColor = true),
    R8(GL_RED, GL_R8, hasColor = true),
    R8_SNORM(GL_RED, GL_R8_SNORM, hasColor = true),
    R16(GL_RED, GL_R16, hasColor = true),
    R16_SNORM(GL_RED, GL_R16_SNORM, hasColor = true),
    RG8(GL_RG, GL_RG8, hasColor = true),
    RG8_SNORM(GL_RG, GL_RG8_SNORM, hasColor = true),
    RG16(GL_RG, GL_RG16, hasColor = true),
    RG16_SNORM(GL_RG, GL_RG16_SNORM, hasColor = true),
    R3_G3_B2(GL_RGB, GL_R3_G3_B2, hasColor = true),
    RGB4(GL_RGB, GL_RGB4, hasColor = true),
    RGB5(GL_RGB, GL_RGB5, hasColor = true),
    RGB565(GL_RGB, GL_RGB565, hasColor = true),
    RGB8(GL_RGB, GL_RGB8, hasColor = true),
    RGB8_SNORM(GL_RGB, GL_RGB8_SNORM, hasColor = true),
    RGB10(GL_RGB, GL_RGB10, hasColor = true),
    RGB12(GL_RGB, GL_RGB12, hasColor = true),
    RGB16(GL_RGB, GL_RGB16, hasColor = true),
    RGB16_SNORM(GL_RGB, GL_RGB16_SNORM, hasColor = true),
    RGBA2(GL_RGBA, GL_RGBA2, hasColor = true),
    RGBA4(GL_RGBA, GL_RGBA4, hasColor = true),
    RGB5_A1(GL_RGBA, GL_RGB5_A1, hasColor = true),
    RGBA8(GL_RGBA, GL_RGBA8, hasColor = true),
    RGBA8_SNORM(GL_RGBA, GL_RGBA8_SNORM, hasColor = true),
    RGB10_A2(GL_RGBA, GL_RGB10_A2, hasColor = true),
    RGB10_A2UI(GL_RGBA_INTEGER, GL_RGB10_A2UI, hasColor = true),
    RGBA12(GL_RGBA, GL_RGBA12, hasColor = true),
    RGBA16(GL_RGBA, GL_RGBA16, hasColor = true),
    RGBA16_SNORM(GL_RGBA, GL_RGBA16_SNORM, hasColor = true),
    SRGB(GL_RGB, GL_SRGB, hasColor = true),
    SRGB8(GL_RGB, GL_SRGB8, hasColor = true),
    SRGB_ALPHA(GL_RGBA, GL_SRGB_ALPHA, hasColor = true),
    SRGB8_ALPHA8(GL_RGBA, GL_SRGB8_ALPHA8, hasColor = true),
    COMPRESSED_SRGB(GL_RGB, GL_COMPRESSED_SRGB, hasColor = true),
    COMPRESSED_SRGB_ALPHA(GL_RGBA, GL_COMPRESSED_SRGB_ALPHA, hasColor = true),
    R16F(GL_RED, GL_R16F, hasColor = true),
    RG16F(GL_RG, GL_RG16F, hasColor = true),
    RGB16F(GL_RGB, GL_RGB16F, hasColor = true),
    RGBA16F(GL_RGBA, GL_RGBA16F, hasColor = true),
    R32F(GL_RED, GL_R32F, hasColor = true),
    RG32F(GL_RG, GL_R32F, hasColor = true),
    RGB32F(GL_RGB, GL_RGB32F, hasColor = true),
    RGB9_E5(GL_RGB, GL_RGB9_E5, hasColor = true),
    RGBA32F(GL_RGBA, GL_RGBA32F, hasColor = true),
    R11F_G11F_B10F(GL_RGBA, GL_R11F_G11F_B10F, hasColor = true),
    R8I(GL_RED_INTEGER, GL_R8I, hasColor = true),
    R8UI(GL_RED_INTEGER, GL_R8UI, hasColor = true),
    R16I(GL_RED_INTEGER, GL_R16I, hasColor = true),
    R16UI(GL_RED_INTEGER, GL_R16UI, hasColor = true),
    R32I(GL_RED_INTEGER, GL_R32I, hasColor = true),
    R32UI(GL_RED_INTEGER, GL_R32UI, hasColor = true),
    RG8I(GL_RG_INTEGER, GL_RG8I, hasColor = true),
    RG8UI(GL_RG_INTEGER, GL_RG8UI, hasColor = true),
    RG16I(GL_RG_INTEGER, GL_RG16I, hasColor = true),
    RG16UI(GL_RG_INTEGER, GL_RG16UI, hasColor = true),
    RG32I(GL_RG_INTEGER, GL_RG32I, hasColor = true),
    RG32UI(GL_RG_INTEGER, GL_RG32UI, hasColor = true),
    RGB8I(GL_RGB_INTEGER, GL_RGB8I, hasColor = true),
    RGB8UI(GL_RGB_INTEGER, GL_RGB8UI, hasColor = true),
    RGB16I(GL_RGB_INTEGER, GL_RGB16I, hasColor = true),
    RGB16UI(GL_RGB_INTEGER, GL_RGB16UI, hasColor = true),
    RGB32I(GL_RGB_INTEGER, GL_RGB32I, hasColor = true),
    RGB32UI(GL_RGB_INTEGER, GL_RGB32UI, hasColor = true),
    RGBA8I(GL_RGBA_INTEGER, GL_RGBA8I, hasColor = true),
    RGBA8UI(GL_RGBA_INTEGER, GL_RGBA8UI, hasColor = true),
    RGBA16I(GL_RGBA_INTEGER, GL_RGBA16I, hasColor = true),
    RGBA16UI(GL_RGBA_INTEGER, GL_RGBA16UI, hasColor = true),
    RGBA32I(GL_RGBA_INTEGER, GL_RGBA32I, hasColor = true),
    RGBA32UI(GL_RGBA_INTEGER, GL_RGBA32UI, hasColor = true),

    DEPTH_COMPONENT(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_FLOAT, hasDepth = true),
    DEPTH_STENCIL(GL_DEPTH_STENCIL, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, hasDepth = true, hasStencil = true),
    DEPTH_COMPONENT16(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT16, GL_FLOAT, hasDepth = true),
    DEPTH_COMPONENT24(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT24, GL_FLOAT, hasDepth = true),
    DEPTH_COMPONENT32(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT32, GL_FLOAT, hasDepth = true),
    DEPTH_COMPONENT32F(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT32F, GL_FLOAT, hasDepth = true),
    DEPTH24_STENCIL8(GL_DEPTH_STENCIL, GL_DEPTH24_STENCIL8, GL_UNSIGNED_INT_24_8, hasDepth = true, hasStencil = true),
    DEPTH32F_STENCIL8(GL_DEPTH_STENCIL, GL_DEPTH32F_STENCIL8, GL_FLOAT_32_UNSIGNED_INT_24_8_REV, hasDepth = true, hasStencil = true);

    override fun glId() = glId
}
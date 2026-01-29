package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL43.*

enum class GlFlag(
    val id: Int,
    val default: Boolean = false,
    private val enable: Runnable = Runnable { glEnable(id) },
    private val disable: Runnable = Runnable { glDisable(id) }
) {
    BLEND(GL_BLEND, enable = GlStateManager::_enableBlend, disable = GlStateManager::_disableBlend),
    COLOR_LOGIC_OP(GL_COLOR_LOGIC_OP, enable = GlStateManager::_enableColorLogicOp, disable = GlStateManager::_disableColorLogicOp),
    CULL_FACE(GL_CULL_FACE, true, enable = GlStateManager::_enableCull, disable = GlStateManager::_disableCull),
    DEBUG_OUTPUT(GL_DEBUG_OUTPUT, true),
    DEBUG_OUTPUT_SYNCHRONOUS(GL_DEBUG_OUTPUT_SYNCHRONOUS, true),
    DEPTH_CLAMP(GL_DEPTH_CLAMP),
    DEPTH_TEST(GL_DEPTH_TEST, true, enable = GlStateManager::_enableDepthTest, disable = GlStateManager::_disableDepthTest),
    DITHER(GL_DITHER),
    FRAMEBUFFER_SRGB(GL_FRAMEBUFFER_SRGB),
    LINE_SMOOTH(GL_LINE_SMOOTH),
    MULTISAMPLE(GL_MULTISAMPLE),
    POLYGON_OFFSET_FILL(GL_POLYGON_OFFSET_FILL, enable = GlStateManager::_enablePolygonOffset, disable = GlStateManager::_disablePolygonOffset),
    POLYGON_OFFSET_LINE(GL_POLYGON_OFFSET_LINE),
    POLYGON_OFFSET_POINT(GL_POLYGON_OFFSET_POINT),
    POLYGON_SMOOTH(GL_POLYGON_SMOOTH),
    PRIMITIVE_RESTART(GL_PRIMITIVE_RESTART),
    PRIMITIVE_RESTART_FIXED_INDEX(GL_PRIMITIVE_RESTART_FIXED_INDEX),
    RASTERIZER_DISCARD(GL_RASTERIZER_DISCARD),
    SAMPLE_ALPHA_TO_COVERAGE(GL_SAMPLE_ALPHA_TO_COVERAGE),
    SAMPLE_ALPHA_TO_ONE(GL_SAMPLE_ALPHA_TO_ONE),
    SAMPLE_COVERAGE(GL_SAMPLE_COVERAGE),
    SAMPLE_SHADING(GL_SAMPLE_SHADING),
    SAMPLE_MASK(GL_SAMPLE_MASK),
    SCISSOR_TEST(GL_SCISSOR_TEST, enable = GlStateManager::_enableScissorTest, disable = GlStateManager::_disableScissorTest),
    STENCIL_TEST(GL_STENCIL_TEST),
    TEXTURE_CUBE_MAP_SEAMLESS(GL_TEXTURE_CUBE_MAP_SEAMLESS),
    PROGRAM_POINT_SIZE(GL_PROGRAM_POINT_SIZE);

    fun enable() {
        enable.run()
    }

    fun disable() {
        disable.run()
    }
}
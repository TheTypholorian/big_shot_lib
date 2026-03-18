package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL43.*

enum class GlFlag(
    override val glId: Int,
    @JvmField
    val raw: GlStateStack<Boolean>
) : GlNamed {
    BLEND(GL_BLEND, NeoGlStateManager.INSTANCE.blendEnabled),
    COLOR_LOGIC_OP(GL_COLOR_LOGIC_OP, NeoGlStateManager.INSTANCE.colorLogicOpEnabled),
    CULL_FACE(GL_CULL_FACE, NeoGlStateManager.INSTANCE.cullFaceEnabled),
    DEBUG_OUTPUT(GL_DEBUG_OUTPUT, NeoGlStateManager.INSTANCE.debugOutputEnabled),
    DEBUG_OUTPUT_SYNCHRONOUS(GL_DEBUG_OUTPUT_SYNCHRONOUS, NeoGlStateManager.INSTANCE.debugOutputSynchronousEnabled),
    DEPTH_CLAMP(GL_DEPTH_CLAMP, NeoGlStateManager.INSTANCE.depthClampEnabled),
    DEPTH_TEST(GL_DEPTH_TEST, NeoGlStateManager.INSTANCE.depthEnabled),
    DITHER(GL_DITHER, NeoGlStateManager.INSTANCE.ditherEnabled),
    FRAMEBUFFER_SRGB(GL_FRAMEBUFFER_SRGB, NeoGlStateManager.INSTANCE.framebufferSRGBEnabled),
    LINE_SMOOTH(GL_LINE_SMOOTH, NeoGlStateManager.INSTANCE.lineSmoothEnabled),
    MULTISAMPLE(GL_MULTISAMPLE, NeoGlStateManager.INSTANCE.multisampleEnabled),
    POLYGON_OFFSET(GL_POLYGON_OFFSET_FILL, NeoGlStateManager.INSTANCE.polygonOffsetEnabled),
    POLYGON_SMOOTH(GL_POLYGON_SMOOTH, NeoGlStateManager.INSTANCE.polygonSmoothEnabled),
    PRIMITIVE_RESTART(GL_PRIMITIVE_RESTART, NeoGlStateManager.INSTANCE.primitiveRestartEnabled),
    PRIMITIVE_RESTART_FIXED_INDEX(GL_PRIMITIVE_RESTART_FIXED_INDEX, NeoGlStateManager.INSTANCE.primitiveRestartFixedIndexEnabled),
    RASTERIZER_DISCARD(GL_RASTERIZER_DISCARD, NeoGlStateManager.INSTANCE.rasterizerDiscardEnabled),
    SAMPLE_ALPHA_TO_COVERAGE(GL_SAMPLE_ALPHA_TO_COVERAGE, NeoGlStateManager.INSTANCE.sampleAlphaToCoverageEnabled),
    SAMPLE_ALPHA_TO_ONE(GL_SAMPLE_ALPHA_TO_ONE, NeoGlStateManager.INSTANCE.sampleAlphaToOneEnabled),
    SAMPLE_COVERAGE(GL_SAMPLE_COVERAGE, NeoGlStateManager.INSTANCE.sampleCoverageEnabled),
    SAMPLE_SHADING(GL_SAMPLE_SHADING, NeoGlStateManager.INSTANCE.sampleShadingEnabled),
    SAMPLE_MASK(GL_SAMPLE_MASK, NeoGlStateManager.INSTANCE.sampleMaskEnabled),
    SCISSOR_TEST(GL_SCISSOR_TEST, NeoGlStateManager.INSTANCE.scissorEnabled),
    STENCIL_TEST(GL_STENCIL_TEST, NeoGlStateManager.INSTANCE.stencilEnabled),
    TEXTURE_CUBE_MAP_SEAMLESS(GL_TEXTURE_CUBE_MAP_SEAMLESS, NeoGlStateManager.INSTANCE.textureCubeMapSeamlessEnabled),
    PROGRAM_POINT_SIZE(GL_PROGRAM_POINT_SIZE, NeoGlStateManager.INSTANCE.programPointSizeEnabled)
}
package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL43.*
import kotlin.reflect.KMutableProperty1

enum class GlFlag(
    override val glId: Int,
    raw: KMutableProperty1<GlStateTracker, Boolean>
) : GlNamed {
    BLEND(GL_BLEND, GlStateTracker::blendEnabled),
    COLOR_LOGIC_OP(GL_COLOR_LOGIC_OP, GlStateTracker::colorLogicOpEnabled),
    CULL_FACE(GL_CULL_FACE, GlStateTracker::cullFaceEnabled),
    DEBUG_OUTPUT(GL_DEBUG_OUTPUT, GlStateTracker::debugOutputEnabled),
    DEBUG_OUTPUT_SYNCHRONOUS(GL_DEBUG_OUTPUT_SYNCHRONOUS, GlStateTracker::debugOutputSynchronousEnabled),
    DEPTH_CLAMP(GL_DEPTH_CLAMP, GlStateTracker::depthClampEnabled),
    DEPTH_TEST(GL_DEPTH_TEST, GlStateTracker::depthEnabled),
    DITHER(GL_DITHER, GlStateTracker::ditherEnabled),
    FRAMEBUFFER_SRGB(GL_FRAMEBUFFER_SRGB, GlStateTracker::framebufferSRGBEnabled),
    LINE_SMOOTH(GL_LINE_SMOOTH, GlStateTracker::lineSmoothEnabled),
    MULTISAMPLE(GL_MULTISAMPLE, GlStateTracker::multisampleEnabled),
    POLYGON_OFFSET(GL_POLYGON_OFFSET_FILL, GlStateTracker::polygonOffsetEnabled),
    POLYGON_SMOOTH(GL_POLYGON_SMOOTH, GlStateTracker::polygonSmoothEnabled),
    PRIMITIVE_RESTART(GL_PRIMITIVE_RESTART, GlStateTracker::primitiveRestartEnabled),
    PRIMITIVE_RESTART_FIXED_INDEX(GL_PRIMITIVE_RESTART_FIXED_INDEX, GlStateTracker::primitiveRestartFixedIndexEnabled),
    RASTERIZER_DISCARD(GL_RASTERIZER_DISCARD, GlStateTracker::rasterizerDiscardEnabled),
    SAMPLE_ALPHA_TO_COVERAGE(GL_SAMPLE_ALPHA_TO_COVERAGE, GlStateTracker::sampleAlphaToCoverageEnabled),
    SAMPLE_ALPHA_TO_ONE(GL_SAMPLE_ALPHA_TO_ONE, GlStateTracker::sampleAlphaToOneEnabled),
    SAMPLE_COVERAGE(GL_SAMPLE_COVERAGE, GlStateTracker::sampleCoverageEnabled),
    SAMPLE_SHADING(GL_SAMPLE_SHADING, GlStateTracker::sampleShadingEnabled),
    SAMPLE_MASK(GL_SAMPLE_MASK, GlStateTracker::sampleMaskEnabled),
    SCISSOR_TEST(GL_SCISSOR_TEST, GlStateTracker::scissorEnabled),
    STENCIL_TEST(GL_STENCIL_TEST, GlStateTracker::stencilEnabled),
    TEXTURE_CUBE_MAP_SEAMLESS(GL_TEXTURE_CUBE_MAP_SEAMLESS, GlStateTracker::textureCubeMapSeamlessEnabled),
    PROGRAM_POINT_SIZE(GL_PROGRAM_POINT_SIZE, GlStateTracker::programPointSizeEnabled);

    val state = GlStateType(raw)

    fun set(value: Boolean?, tracker: GlStateTracker = OpenGL.INSTANCE) {
        state.raw.set(tracker, value ?: false)
    }
}
package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL43.*

enum class GlFlag(
    override val glId: Int
) : GlNamed {
    BLEND(GL_BLEND),
    COLOR_LOGIC_OP(GL_COLOR_LOGIC_OP),
    CULL_FACE(GL_CULL_FACE),
    DEBUG_OUTPUT(GL_DEBUG_OUTPUT),
    DEBUG_OUTPUT_SYNCHRONOUS(GL_DEBUG_OUTPUT_SYNCHRONOUS),
    DEPTH_CLAMP(GL_DEPTH_CLAMP),
    DEPTH_TEST(GL_DEPTH_TEST),
    DITHER(GL_DITHER),
    FRAMEBUFFER_SRGB(GL_FRAMEBUFFER_SRGB),
    LINE_SMOOTH(GL_LINE_SMOOTH),
    MULTISAMPLE(GL_MULTISAMPLE),
    POLYGON_OFFSET(GL_POLYGON_OFFSET_FILL),
    POLYGON_SMOOTH(GL_POLYGON_SMOOTH),
    PRIMITIVE_RESTART(GL_PRIMITIVE_RESTART),
    PRIMITIVE_RESTART_FIXED_INDEX(GL_PRIMITIVE_RESTART_FIXED_INDEX),
    RASTERIZER_DISCARD(GL_RASTERIZER_DISCARD),
    SAMPLE_ALPHA_TO_COVERAGE(GL_SAMPLE_ALPHA_TO_COVERAGE),
    SAMPLE_ALPHA_TO_ONE(GL_SAMPLE_ALPHA_TO_ONE),
    SAMPLE_COVERAGE(GL_SAMPLE_COVERAGE),
    SAMPLE_SHADING(GL_SAMPLE_SHADING),
    SAMPLE_MASK(GL_SAMPLE_MASK),
    SCISSOR_TEST(GL_SCISSOR_TEST),
    STENCIL_TEST(GL_STENCIL_TEST),
    TEXTURE_CUBE_MAP_SEAMLESS(GL_TEXTURE_CUBE_MAP_SEAMLESS),
    PROGRAM_POINT_SIZE(GL_PROGRAM_POINT_SIZE);

    val stack = GlStateManager<Boolean?>(name, this::set) { OpenGL.INSTANCE.isEnabled(this) }

    fun queryValue() = stack.query()

    fun set(value: Boolean?) {
        if (value ?: false) {
            enable()
        } else {
            disable()
        }
    }

    fun enable() {
        OpenGL.INSTANCE.enable(this)
    }

    fun disable() {
        OpenGL.INSTANCE.disable(this)
    }
}
package net.typho.big_shot_lib.state.gl

import com.mojang.serialization.Codec
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.*

enum class BlendFactor(val id: Int) {
    ZERO(GL_ZERO),
    ONE(GL_ONE),

    SRC_COLOR(GL_SRC_COLOR),
    ONE_MINUS_SRC_COLOR(GL_ONE_MINUS_SRC_COLOR),
    DST_COLOR(GL_DST_COLOR),
    ONE_MINUS_DST_COLOR(GL_ONE_MINUS_DST_COLOR),
    CONSTANT_COLOR(GL_CONSTANT_COLOR),
    ONE_MINUS_CONSTANT_COLOR(GL_ONE_MINUS_CONSTANT_COLOR),

    SRC_ALPHA(GL_SRC_ALPHA),
    ONE_MINUS_SRC_ALPHA(GL_ONE_MINUS_SRC_ALPHA),
    DST_ALPHA(GL_DST_ALPHA),
    ONE_MINUS_DST_ALPHA(GL_ONE_MINUS_DST_ALPHA),
    CONSTANT_ALPHA(GL_CONSTANT_ALPHA),
    ONE_MINUS_CONSTANT_ALPHA(GL_ONE_MINUS_CONSTANT_ALPHA),

    SRC_ALPHA_SATURATE(GL_SRC_ALPHA_SATURATE);

    companion object {
        @JvmField
        val CODEC: Codec<BlendFactor> = Codec.STRING.xmap(
            { key -> enumValueOf<BlendFactor>(key) },
            { factor -> factor.name }
        )
    }
}
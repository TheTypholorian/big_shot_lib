package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL14.*

class BlendFunction(
    var src: BlendFactor,
    var dst: BlendFactor
) : GlState<BlendFunction>, GlState.Value {
    companion object {
        @JvmField
        val DEFAULT = BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)
    }

    override fun default() = DEFAULT

    override fun queryValue(): BlendFunction? {
        val src = glGetInteger(GL_BLEND_SRC_RGB)
        val dst = glGetInteger(GL_BLEND_DST_RGB)
        return BlendFunction(
            BlendFactor.entries.find { it.id == src }!!,
            BlendFactor.entries.find { it.id == dst }!!
        )
    }

    override fun set(value: BlendFunction) {
        GlStateManager._blendFunc(value.src.id, value.dst.id)
    }

    override fun getType() = this
}

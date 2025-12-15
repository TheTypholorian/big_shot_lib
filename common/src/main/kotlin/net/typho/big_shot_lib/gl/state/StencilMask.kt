package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.GL_STENCIL_WRITEMASK
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL20.glStencilMaskSeparate

object StencilMask : GlState<StencilMask.Mask> {
    override fun default() = Mask.DEFAULT

    override fun queryValue(): Mask? {
        return Mask(glGetInteger(GL_STENCIL_WRITEMASK))
    }

    override fun set(value: Mask) {
        glStencilMaskSeparate(value.face.id, value.mask)
    }

    class Mask(
        var mask: Int,
        var face: Face = Face.FRONT_AND_BACK
    ) {
        companion object {
            val DEFAULT = Mask(0xFF)
        }
    }
}

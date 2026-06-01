package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram

interface NeoGuiGraphics {
    fun blitWithShader(shader: GlProgram, location: Identifier, x: Int, y: Int, uOffset: Int, vOffset: Int, uWidth: Int, vHeight: Int)

    fun blitWithShader(shader: GlProgram, location: Identifier, x: Int, y: Int, blitOffset: Int, uOffset: Float, vOffset: Float, uWidth: Int, vHeight: Int, textureWidth: Int, textureHeight: Int)

    fun blitWithShader(shader: GlProgram, location: Identifier, x: Int, y: Int, width: Int, height: Int, uOffset: Float, vOffset: Float, uWidth: Int, vHeight: Int, textureWidth: Int, textureHeight: Int)

    fun blitWithShader(shader: GlProgram, location: Identifier, x: Int, y: Int, uOffset: Float, vOffset: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int)
}
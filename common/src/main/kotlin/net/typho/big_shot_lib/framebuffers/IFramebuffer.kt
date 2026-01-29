package net.typho.big_shot_lib.framebuffers

import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import net.typho.big_shot_lib.textures.TextureFormat
import org.joml.Vector4f

interface IFramebuffer : GlResourceInstance {
    fun colorFormat(): TextureFormat

    fun depthFormat(): TextureFormat?

    fun width(): Int

    fun height(): Int

    fun resize(width: Int, height: Int)

    fun clearColor(color: Vector4f)
}
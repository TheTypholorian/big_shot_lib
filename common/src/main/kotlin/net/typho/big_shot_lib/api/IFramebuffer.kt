package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.Bindable
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import net.typho.big_shot_lib.gl.resource.TextureFormat
import org.joml.Vector4f

interface IFramebuffer : Bindable, GlResourceInstance {
    fun colorFormat(): TextureFormat

    fun depthFormat(): TextureFormat?

    fun width(): Int

    fun height(): Int

    fun resize(width: Int, height: Int)

    fun clearColor(color: Vector4f)
}
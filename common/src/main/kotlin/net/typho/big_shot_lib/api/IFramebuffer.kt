package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.Bindable
import net.typho.big_shot_lib.gl.GlResourceInstance
import net.typho.big_shot_lib.gl.TextureFormat

interface IFramebuffer : Bindable, GlResourceInstance {
    fun colorFormat(): TextureFormat

    fun depthFormat(): TextureFormat?

    fun width(): Int

    fun height(): Int

    fun resize(width: Int, height: Int)
}
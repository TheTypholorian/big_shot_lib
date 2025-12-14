package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.Bindable
import net.typho.big_shot_lib.gl.BufferUsage
import net.typho.big_shot_lib.gl.GlResourceInstance

interface IBuffer : Bindable, GlResourceInstance {
    fun usage(): BufferUsage

    fun upload(buffer: Long)
}
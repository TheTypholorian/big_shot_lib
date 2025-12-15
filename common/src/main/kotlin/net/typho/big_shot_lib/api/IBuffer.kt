package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.Bindable
import net.typho.big_shot_lib.gl.resource.BufferUsage
import net.typho.big_shot_lib.gl.resource.GlResourceInstance

interface IBuffer : Bindable, GlResourceInstance {
    fun usage(): BufferUsage

    fun upload(buffer: Long)
}
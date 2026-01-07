package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.gl.resource.BufferUsage
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import java.nio.ByteBuffer

interface IBuffer : GlResourceInstance {
    fun usage(): BufferUsage

    fun upload(buffer: ByteBuffer)
}
package net.typho.big_shot_lib.impl.client.rendering.opengl

import com.mojang.blaze3d.buffers.GpuBufferSlice

interface GlShaderStorageBufferStorage {
    val `big_shot_lib$shaderStorageBuffers`: MutableMap<String, GpuBufferSlice>

    fun `big_shot_lib$getShaderStorageBufferBinding`(name: String): Int
}
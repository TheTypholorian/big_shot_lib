package net.typho.big_shot_lib.api.peer

import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.vertex.VertexFormat

interface ShaderInstanceUnsafeExtension {
    fun setProgramId(id: Int)

    fun setName(name: String)

    fun setVertex(program: Program)

    fun setFragment(program: Program)

    fun setFormat(format: VertexFormat)

    fun init()
}
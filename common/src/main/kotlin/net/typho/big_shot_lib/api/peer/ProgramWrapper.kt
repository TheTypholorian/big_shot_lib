package net.typho.big_shot_lib.api.peer

import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.shaders.Shader

class ProgramWrapper(type: Type, id: Int, name: String) : Program(type, id, name) {
    constructor(type: Type, name: String) : this(type, -1, name)

    override fun attachToShader(shader: Shader) {
        throw UnsupportedOperationException()
    }

    override fun close() {
    }
}
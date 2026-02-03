package net.typho.big_shot_lib.api.shaders.variables

import net.typho.big_shot_lib.api.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.shaders.mixins.ShaderOpcode
import java.nio.IntBuffer

object ShaderAnyType : ShaderVariableType {
    override fun matches(
        opcode: ShaderOpcode,
        id: Int?
    ): Boolean {
        if (opcode.id < 19 || opcode.id > 39) {
            return false
        }

        if (id != null && opcode.getWord(0) != id) {
            return false
        }

        return true
    }

    override fun compile(
        id: Int,
        context: ShaderBytecodeBuffer
    ): IntBuffer {
        throw UnsupportedOperationException()
    }
}
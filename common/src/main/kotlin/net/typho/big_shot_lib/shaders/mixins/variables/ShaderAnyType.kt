package net.typho.big_shot_lib.shaders.mixins.variables

import net.typho.big_shot_lib.shaders.mixins.Opcode
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinContext
import java.nio.ByteBuffer

object ShaderAnyType : ShaderVariableType {
    override fun matches(
        opcode: Opcode,
        context: ShaderMixinContext,
        id: Int?
    ): Boolean {
        if (opcode.id < 19 || opcode.id > 39) {
            return false
        }

        if (id != null && context.code.getInt((opcode.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES) != id) {
            return false
        }

        return true
    }

    override fun compile(
        id: Int,
        context: ShaderMixinContext
    ): ByteBuffer {
        throw UnsupportedOperationException()
    }
}
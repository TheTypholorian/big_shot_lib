package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.ShaderMixinContext.Companion.WORD_SIZE_BYTES
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

        if (id != null && context.code.getInt((opcode.index + 1) * WORD_SIZE_BYTES) != id) {
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
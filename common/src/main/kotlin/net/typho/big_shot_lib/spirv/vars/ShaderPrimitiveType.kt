package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.ShaderMixinContext.Companion.WORD_SIZE_BYTES
import java.nio.ByteBuffer

enum class ShaderPrimitiveType(
    val id: Int,
    val width: Int? = null,
    val signed: Boolean? = null
) : ShaderVariableType {
    VOID(19),
    BOOLEAN(20),

    INT_8(21, 8, true),
    UINT_8(21, 8, false),
    INT_16(21, 16, true),
    UINT_16(21, 16, false),
    INT_32(21, 32, true),
    UINT_32(21, 32, false),

    FLOAT_8(22, 8),
    FLOAT_16(22, 16),
    FLOAT_32(22, 32);

    override fun matches(opcode: Opcode, context: ShaderMixinContext): Boolean {
        if (opcode.id != id) {
            return false
        }

        var pointer = opcode.index + 2

        width?.let {
            if (context.code.getInt(pointer * WORD_SIZE_BYTES) != width) {
                return false
            }

            pointer++
        }

        signed?.let {
            if (context.code.getInt(pointer * WORD_SIZE_BYTES) == 1 != signed) {
                return false
            }
        }

        return true
    }

    override fun compile(id: Int, context: ShaderMixinContext): ByteBuffer {
        var size = 2

        width?.let { size++ }
        signed?.let { size++ }

        val buffer = ByteBuffer.allocate(size * WORD_SIZE_BYTES)
            .order(ShaderMixinContext.BYTE_ORDER)

            .putInt((size shl 16) or this.id)
            .putInt(id)

        width?.let {
            buffer.putInt(width)
        }

        signed?.let {
            buffer.putInt(if (signed) 1 else 0)
        }

        return buffer
    }
}
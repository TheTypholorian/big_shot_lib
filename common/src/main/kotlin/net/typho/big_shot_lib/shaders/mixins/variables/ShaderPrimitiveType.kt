package net.typho.big_shot_lib.shaders.mixins.variables

import net.typho.big_shot_lib.shaders.mixins.Opcode
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinContext
import java.nio.ByteBuffer

enum class ShaderPrimitiveType(
    val id: Int,
    val width: Int? = null,
    val signed: Boolean? = null
) : ShaderVariableType {
    VOID(Opcode.OP_TYPE_VOID),
    BOOLEAN(Opcode.OP_TYPE_BOOL),

    INT_8(Opcode.OP_TYPE_INT, 8, true),
    UINT_8(Opcode.OP_TYPE_INT, 8, false),
    INT_16(Opcode.OP_TYPE_INT, 16, true),
    UINT_16(Opcode.OP_TYPE_INT, 16, false),
    INT_32(Opcode.OP_TYPE_INT, 32, true),
    UINT_32(Opcode.OP_TYPE_INT, 32, false),

    FLOAT_8(Opcode.OP_TYPE_FLOAT, 8),
    FLOAT_16(Opcode.OP_TYPE_FLOAT, 16),
    FLOAT_32(Opcode.OP_TYPE_FLOAT, 32);

    override fun matches(
        opcode: Opcode,
        context: ShaderMixinContext,
        id: Int?
    ): Boolean {
        if (opcode.id != this.id) {
            return false
        }

        if (id != null && context.code.getInt((opcode.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES) != id) {
            return false
        }

        var pointer = opcode.index + 2

        width?.let {
            if (context.code.getInt(pointer * ShaderMixinContext.WORD_SIZE_BYTES) != width) {
                return false
            }

            pointer++
        }

        signed?.let {
            if (context.code.getInt(pointer * ShaderMixinContext.WORD_SIZE_BYTES) == 1 != signed) {
                return false
            }
        }

        return true
    }

    override fun compile(id: Int, context: ShaderMixinContext): ByteBuffer {
        var size = 2

        width?.let { size++ }
        signed?.let { size++ }

        val buffer = ByteBuffer.allocate(size * ShaderMixinContext.WORD_SIZE_BYTES)
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
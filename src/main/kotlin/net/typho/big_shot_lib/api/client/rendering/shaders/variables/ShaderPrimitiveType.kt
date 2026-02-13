package net.typho.big_shot_lib.api.client.rendering.shaders.variables

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderOpcode
import java.nio.IntBuffer

enum class ShaderPrimitiveType(
    val id: Int,
    val width: Int? = null,
    val signed: Boolean? = null
) : ShaderVariableType {
    VOID(ShaderOpcode.OP_TYPE_VOID),
    BOOLEAN(ShaderOpcode.OP_TYPE_BOOL),

    INT_8(ShaderOpcode.OP_TYPE_INT, 8, true),
    UINT_8(ShaderOpcode.OP_TYPE_INT, 8, false),
    INT_16(ShaderOpcode.OP_TYPE_INT, 16, true),
    UINT_16(ShaderOpcode.OP_TYPE_INT, 16, false),
    INT_32(ShaderOpcode.OP_TYPE_INT, 32, true),
    UINT_32(ShaderOpcode.OP_TYPE_INT, 32, false),

    FLOAT_8(ShaderOpcode.OP_TYPE_FLOAT, 8),
    FLOAT_16(ShaderOpcode.OP_TYPE_FLOAT, 16),
    FLOAT_32(ShaderOpcode.OP_TYPE_FLOAT, 32);

    override fun matches(
        opcode: ShaderOpcode,
        id: Int?
    ): Boolean {
        if (opcode.id != this.id) {
            return false
        }

        if (id != null && opcode.getWord(0) != id) {
            return false
        }

        var pointer = opcode.index + 1

        width?.let {
            if (opcode.parent.buffer.get(pointer) != width) {
                return false
            }

            pointer++
        }

        signed?.let {
            if (opcode.parent.buffer.get(pointer) == 1 != signed) {
                return false
            }
        }

        return true
    }

    override fun compile(id: Int, context: ShaderBytecodeBuffer): IntBuffer {
        return ShaderOpcode.Builder(this.id)
            .putWord(id)
            .putWord(width)
            .putWord(signed?.let { if (it) 1 else 0 })
            .build()
    }
}
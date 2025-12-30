package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.ShaderMixinContext.Companion.WORD_SIZE_BYTES
import net.typho.big_shot_lib.spirv.at.At
import java.nio.ByteBuffer

class ShaderMatrixType(
    val component: ShaderPrimitiveType,
    val size: Int
) : ShaderVariableType {
    override fun matches(
        opcode: Opcode,
        context: ShaderMixinContext
    ): Boolean {
        if (opcode.id != 23) {
            return false
        }

        if (context.code.getInt((opcode.index + 3) * WORD_SIZE_BYTES) != size) {
            return false
        }

        val inner = context.code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)

        for (opcode1 in context) {
            if (component.matches(opcode1, context)) {
                if (context.code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES) == inner) {
                    return true
                }
            }
        }

        return false
    }

    override fun findOrInject(context: ShaderMixinContext, at: At): Int {
        for (opcode in context) {
            if (matches(opcode, context)) {
                return context.code.getInt((opcode.index + 1) * WORD_SIZE_BYTES)
            }
        }

        for (opcode in context) {
            if (component.matches(opcode, context)) {
                val id = context.bound++
                context.putBound()
                val id1 = context.code.getInt((opcode.index + 1) * WORD_SIZE_BYTES)

                context.inject(
                    at,
                    ByteBuffer.allocate(4 * WORD_SIZE_BYTES)
                        .order(ShaderMixinContext.BYTE_ORDER)

                        .putInt(0x00_04_00_18)
                        .putInt(id)
                        .putInt(id1)
                        .putInt(size)
                )
                return id
            }
        }

        return inject(context, at)
    }

    override fun compile(id: Int, context: ShaderMixinContext): ByteBuffer {
        val id1 = context.bound++
        context.putBound()

        val buffer = component.compile(id1, context)
        return ByteBuffer.allocate(buffer.capacity() + 4 * WORD_SIZE_BYTES)
            .order(ShaderMixinContext.BYTE_ORDER)

            .put(0, buffer, 0, buffer.capacity())
            .position(buffer.capacity())

            .putInt(0x00_04_00_18)
            .putInt(id)
            .putInt(id1)
            .putInt(size)
    }
}
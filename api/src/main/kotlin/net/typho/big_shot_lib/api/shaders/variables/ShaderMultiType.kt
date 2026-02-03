package net.typho.big_shot_lib.api.shaders.variables

import net.typho.big_shot_lib.api.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.shaders.mixins.ShaderOpcode
import net.typho.big_shot_lib.api.util.BufferUtil.concat
import java.nio.IntBuffer

open class ShaderMultiType(
    val id: Int,
    val component: ShaderPrimitiveType,
    val size: Int
) : ShaderVariableType {
    override fun matches(
        opcode: ShaderOpcode,
        id: Int?
    ): Boolean {
        if (opcode.id != this.id) {
            return false
        }

        if (opcode.getWord(2) != size) {
            return false
        }

        if (id != null && opcode.getWord(0) != id) {
            return false
        }

        val inner = opcode.getWord(1)

        return opcode.parent.findOpcode { opcode -> component.matches(opcode) && opcode.getWord(0) == inner } != null
    }

    override fun findOrInject(buffer: ShaderBytecodeBuffer, words: Int): Int {
        for (opcode in buffer.opcodes()) {
            if (component.matches(opcode)) {
                return opcode.getWord(0)
            }
        }

        for (opcode in buffer.opcodes()) {
            if (component.matches(opcode)) {
                val id = buffer.bound++
                val id1 = opcode.getWord(0)

                buffer.insert(
                    words,
                    ShaderOpcode.Builder(this.id)
                        .putWord(id)
                        .putWord(id1)
                        .putWord(size)
                        .build()
                )
                return id
            }
        }

        return inject(buffer, words)
    }

    override fun compile(id: Int, context: ShaderBytecodeBuffer): IntBuffer {
        val id1 = context.bound++

        return component.compile(id1, context).concat(
            ShaderOpcode.Builder(this.id)
                .putWord(id)
                .putWord(id1)
                .putWord(size)
                .build()
        )
    }
}
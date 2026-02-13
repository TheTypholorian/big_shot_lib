package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderOpcode
import java.nio.IntBuffer

sealed interface ShaderVariableCategory {
    /*
    PRIMITIVE,
    VECTOR,
    MATRIX,
    SAMPLER,
    IMAGE
     */

    @JvmRecord
    data class Primitive(
        @JvmField
        val opcodeId: Int
    ) : ShaderVariableCategory {
        fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int? = null): Boolean {
            return opcode.id == opcodeId && expectedId?.let {  }
        }

        fun findOrInjectBytecode(buffer: ShaderBytecodeBuffer, index: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
            buffer.findOpcode { matchesBytecode(it) }?.let {
                return it.getWord(0)
            }

            return injectBytecode(buffer, index)
        }

        fun injectBytecode(buffer: ShaderBytecodeBuffer, words: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
            val id = buffer.bound++
            buffer.insert(words, compile(id, buffer))
            return id
        }

        fun compile(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer {
        }
    }
}
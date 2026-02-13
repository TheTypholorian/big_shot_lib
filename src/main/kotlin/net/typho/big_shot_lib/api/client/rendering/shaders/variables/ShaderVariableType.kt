package net.typho.big_shot_lib.api.client.rendering.shaders.variables

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderOpcode
import java.nio.IntBuffer

interface ShaderVariableType {
    fun matches(opcode: ShaderOpcode, id: Int? = null): Boolean

    fun findOrInject(buffer: ShaderBytecodeBuffer, words: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
        buffer.findOpcode { matches(it) }?.let {
            return it.getWord(0)
        }

        return inject(buffer, words)
    }

    fun inject(context: ShaderBytecodeBuffer, words: Int = context.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
        val id = context.bound++
        context.insert(words, compile(id, context))
        return id
    }

    fun compile(id: Int, context: ShaderBytecodeBuffer): IntBuffer
}
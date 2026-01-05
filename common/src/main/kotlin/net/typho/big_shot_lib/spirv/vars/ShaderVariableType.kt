package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.ShaderMixinContext.Companion.WORD_SIZE_BYTES
import java.nio.ByteBuffer

interface ShaderVariableType {
    fun matches(opcode: Opcode, context: ShaderMixinContext, id: Int? = null): Boolean

    fun findOrInject(context: ShaderMixinContext, words: Int = context.locateOpcode(Opcode.OP_FUNCTION)!!.index): Int {
        for (opcode in context) {
            if (matches(opcode, context)) {
                return context.code.getInt((opcode.index + 1) * WORD_SIZE_BYTES)
            }
        }

        return inject(context, words)
    }

    fun inject(context: ShaderMixinContext, words: Int = context.locateOpcode(Opcode.OP_FUNCTION)!!.index): Int {
        val id = context.bound++
        context.putBound()
        context.inject(words, compile(id, context))
        return id
    }

    fun compile(id: Int, context: ShaderMixinContext): ByteBuffer
}
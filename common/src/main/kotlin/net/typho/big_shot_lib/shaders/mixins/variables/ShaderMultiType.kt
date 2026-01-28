package net.typho.big_shot_lib.shaders.mixins.variables

import net.typho.big_shot_lib.shaders.mixins.Opcode
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinContext
import net.typho.big_shot_lib.util.BufferUtil
import java.nio.ByteBuffer

open class ShaderMultiType(
    val id: Int,
    val component: ShaderPrimitiveType,
    val size: Int
) : ShaderVariableType {
    override fun matches(
        opcode: Opcode,
        context: ShaderMixinContext,
        id: Int?
    ): Boolean {
        if (opcode.id != this.id) {
            return false
        }

        if (context.code.getInt((opcode.index + 3) * ShaderMixinContext.WORD_SIZE_BYTES) != size) {
            return false
        }

        if (id != null && context.code.getInt((opcode.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES) != id) {
            return false
        }

        val inner = context.code.getInt((opcode.index + 2) * ShaderMixinContext.WORD_SIZE_BYTES)

        for (opcode1 in context) {
            if (component.matches(opcode1, context)) {
                if (context.code.getInt((opcode1.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES) == inner) {
                    return true
                }
            }
        }

        return false
    }

    override fun findOrInject(context: ShaderMixinContext, words: Int): Int {
        for (opcode in context) {
            if (matches(opcode, context)) {
                return context.code.getInt((opcode.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES)
            }
        }

        for (opcode in context) {
            if (component.matches(opcode, context)) {
                val id = context.bound++
                context.putBound()
                val id1 = context.code.getInt((opcode.index + 1) * ShaderMixinContext.WORD_SIZE_BYTES)

                context.inject(
                    words,
                    Opcode.Builder(this.id)
                        .putInt(id)
                        .putInt(id1)
                        .putInt(size)
                        .build()
                )
                return id
            }
        }

        return inject(context, words)
    }

    override fun compile(id: Int, context: ShaderMixinContext): ByteBuffer {
        val id1 = context.bound++
        context.putBound()

        return BufferUtil.concat(
            component.compile(id1, context),
            Opcode.Builder(this.id)
                .putInt(id)
                .putInt(id1)
                .putInt(size)
                .build()
        )
    }
}
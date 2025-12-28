package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.at.At
import net.typho.big_shot_lib.spirv.at.BeforeFirstFunction
import java.nio.ByteBuffer

interface ShaderVariableType {
    fun matches(opcode: Opcode, context: ShaderMixinContext): Boolean

    fun inject(context: ShaderMixinContext, at: At = BeforeFirstFunction): Int {
        val id = context.bound++
        context.putBound()
        context.inject(at, compile(id, context))
        return id
    }

    fun compile(id: Int, context: ShaderMixinContext): ByteBuffer
}
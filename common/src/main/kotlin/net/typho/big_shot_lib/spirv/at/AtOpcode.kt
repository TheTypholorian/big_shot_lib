package net.typho.big_shot_lib.spirv.at

import net.typho.big_shot_lib.spirv.ShaderMixinContext

open class AtOpcode(val id: Int) : At {
    override fun getStart(context: ShaderMixinContext): Int {
        for (opcode in context) {
            if (opcode.id == id) {
                return opcode.index
            }
        }

        return 0
    }

    override fun getEnd(context: ShaderMixinContext): Int {
        for (opcode in context) {
            if (opcode.id == id) {
                return opcode.index + opcode.length + 1
            }
        }

        return 0
    }
}
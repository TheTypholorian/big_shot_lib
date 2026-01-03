package net.typho.big_shot_lib.spirv.at

import net.typho.big_shot_lib.error.ShaderMixinException
import net.typho.big_shot_lib.spirv.ShaderMixinContext

open class AtOpcodeInMethod(val id: Int, val method: String): At {
    override fun getStart(context: ShaderMixinContext): Int {
        var methodId: Int? = -1

        for (opcode in context) {
            if (opcode.id == 5) { // OpName
                val contents = context.getOpcodeData(opcode)
                val contentsArray = contents.array()
                val name = String(contentsArray,
                    ShaderMixinContext.Companion.WORD_SIZE_BYTES, contentsArray.size - 2 * ShaderMixinContext.Companion.WORD_SIZE_BYTES
                )

                if (name.trim(0.toChar()) == method) {
                    methodId = contents.getInt(0)
                    break
                }
            }
        }

        if (methodId == -1) {
            throw ShaderMixinException("Unable to find method $method")
        }

        var inMethod = false

        for (opcode in context) {
            if (opcode.id == 54) { // OpFunction
                if (inMethod) {
                    break
                }

                val contents = context.getOpcodeData(opcode)

                if (contents.getInt(ShaderMixinContext.Companion.WORD_SIZE_BYTES) == methodId) {
                    inMethod = true
                }
            } else if (inMethod && opcode.id == id) {
                return opcode.index
            }
        }

        throw ShaderMixinException("Unable to find opcode $id method $method")
    }
}
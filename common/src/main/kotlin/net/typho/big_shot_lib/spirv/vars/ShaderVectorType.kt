package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode

class ShaderVectorType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(Opcode.OP_TYPE_VECTOR, component, size)
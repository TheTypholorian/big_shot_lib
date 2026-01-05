package net.typho.big_shot_lib.spirv.vars

import net.typho.big_shot_lib.spirv.Opcode

class ShaderMatrixType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(Opcode.OP_TYPE_MATRIX, component, size)
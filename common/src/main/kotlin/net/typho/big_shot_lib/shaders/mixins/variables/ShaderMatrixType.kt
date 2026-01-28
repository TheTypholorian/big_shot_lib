package net.typho.big_shot_lib.shaders.mixins.variables

import net.typho.big_shot_lib.shaders.mixins.Opcode

class ShaderMatrixType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(Opcode.OP_TYPE_MATRIX, component, size)
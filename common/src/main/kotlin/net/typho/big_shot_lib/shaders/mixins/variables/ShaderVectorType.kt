package net.typho.big_shot_lib.shaders.mixins.variables

import net.typho.big_shot_lib.shaders.mixins.Opcode

class ShaderVectorType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(Opcode.OP_TYPE_VECTOR, component, size)
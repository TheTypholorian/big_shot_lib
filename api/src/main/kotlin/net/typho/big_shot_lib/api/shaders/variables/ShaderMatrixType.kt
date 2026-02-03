package net.typho.big_shot_lib.api.shaders.variables

import net.typho.big_shot_lib.api.shaders.mixins.ShaderOpcode

class ShaderMatrixType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(ShaderOpcode.OP_TYPE_MATRIX, component, size)
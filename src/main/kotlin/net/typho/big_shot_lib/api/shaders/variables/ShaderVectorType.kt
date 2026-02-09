package net.typho.big_shot_lib.api.shaders.variables

import net.typho.big_shot_lib.api.shaders.mixins.ShaderOpcode

open class ShaderVectorType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(ShaderOpcode.OP_TYPE_VECTOR, component, size)
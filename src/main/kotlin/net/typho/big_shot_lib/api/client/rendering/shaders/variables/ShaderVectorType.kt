package net.typho.big_shot_lib.api.client.rendering.shaders.variables

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderOpcode

open class ShaderVectorType(
    component: ShaderPrimitiveType,
    size: Int
) : ShaderMultiType(ShaderOpcode.OP_TYPE_VECTOR, component, size)
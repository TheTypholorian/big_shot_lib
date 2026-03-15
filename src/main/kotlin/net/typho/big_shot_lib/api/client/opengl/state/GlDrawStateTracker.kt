package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.NeoColor

interface GlDrawStateTracker {
    var blendColor: NeoColor
    var blendEquation: BlendEquation
    var blendFunction: BlendFunction
    var colorMask: ColorMask
    var cullFace: CullFace
    var depthMask: Boolean
    var depthFunc: ComparisonFunc
    var polygonMode: PolygonMode
    var polygonOffset: PolygonOffset
    var scissor: AbstractRect2<Int, *, *>
    var stencilFunction: StencilFunction
    var stencilMask: Int
    var stencilOp: StencilOp
    var viewport: AbstractRect2<Int, *, *>
}
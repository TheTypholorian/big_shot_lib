package net.typho.big_shot_lib.gl.state

import java.util.*

interface GlState<T> {
    fun default(): T

    fun queryValue(): T?

    fun set(value: T)

    @Suppress("UNCHECKED_CAST")
    fun setCast(value: Any) = set(value as T)

    fun reset() = set(default())

    companion object {
        val ALL_STATES = LinkedList<GlState<*>>(listOf(
            BlendColor,
            BlendEquation,
            BlendFunction,
            ColorMask,
            CullFace,
            DepthTest,
            LineWidth,
            PointSize,
            PolygonMode,
            Scissor,
            StencilFunc,
            StencilMask,
            StencilOp
        ))
    }
}
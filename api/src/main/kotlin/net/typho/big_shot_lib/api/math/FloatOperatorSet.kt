package net.typho.big_shot_lib.api.math.op

import org.lwjgl.system.MemoryUtil.memPutFloat

object FloatOperatorSet : OperatorSet<Float> {
    override val zero: Float = 0f
    override val one: Float = 1f
    override val byteSize: Int = 4

    override fun put(pointer: Long, a: Float) {
        memPutFloat(pointer, a)
    }

    override fun lerp(a: Float, b: Float, d: Float): Float {
        return a + d * (b - a)
    }

    override fun plus(a: Float, b: Float): Float {
        return a + b
    }

    override fun minus(a: Float, b: Float): Float {
        return a - b
    }

    override fun times(a: Float, b: Float): Float {
        return a * b
    }

    override fun div(a: Float, b: Float): Float {
        return a / b
    }

    override fun rem(a: Float, b: Float): Float {
        return a % b
    }

    override fun sqrt(a: Float): Float {
        return kotlin.math.sqrt(a)
    }

    override fun min(a: Float, b: Float): Float {
        return kotlin.math.min(a, b)
    }

    override fun max(a: Float, b: Float): Float {
        return kotlin.math.max(a, b)
    }

    override fun lessThan(a: Float, b: Float): Boolean {
        return a < b
    }

    override fun lequalThan(a: Float, b: Float): Boolean {
        return a <= b
    }

    override fun greaterThan(a: Float, b: Float): Boolean {
        return a > b
    }

    override fun gequalThan(a: Float, b: Float): Boolean {
        return a >= b
    }

    override fun abs(a: Float): Float {
        return kotlin.math.abs(a)
    }

    override fun negate(a: Float): Float {
        return -a
    }
}
package net.typho.big_shot_lib.api.math.op

object IntOperatorSet : OperatorSet<Int> {
    override val zero: Int = 0
    override val one: Int = 1

    override fun fromInt(a: Int): Int {
        return a
    }

    override fun plus(a: Int, b: Int): Int {
        return a + b
    }

    override fun minus(a: Int, b: Int): Int {
        return a - b
    }

    override fun times(a: Int, b: Int): Int {
        return a * b
    }

    override fun div(a: Int, b: Int): Int {
        return a / b
    }

    override fun rem(a: Int, b: Int): Int {
        return a % b
    }

    override fun sqrt(a: Int): Float {
        return kotlin.math.sqrt(a.toFloat())
    }

    override fun min(a: Int, b: Int): Int {
        return kotlin.math.min(a, b)
    }

    override fun max(a: Int, b: Int): Int {
        return kotlin.math.max(a, b)
    }

    override fun lessThan(a: Int, b: Int): Boolean {
        return a < b
    }

    override fun lequalThan(a: Int, b: Int): Boolean {
        return a <= b
    }

    override fun greaterThan(a: Int, b: Int): Boolean {
        return a > b
    }

    override fun gequalThan(a: Int, b: Int): Boolean {
        return a >= b
    }

    override fun abs(a: Int): Int {
        return kotlin.math.abs(a)
    }

    override fun negate(a: Int): Int {
        return -a
    }
}
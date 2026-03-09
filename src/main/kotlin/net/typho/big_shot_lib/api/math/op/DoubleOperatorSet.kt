package net.typho.big_shot_lib.api.math.op

object DoubleOperatorSet : OperatorSet<Double> {
    override val zero: Double = 0.0
    override val one: Double = 1.0

    override fun plus(a: Double, b: Double): Double {
        return a + b
    }

    override fun minus(a: Double, b: Double): Double {
        return a - b
    }

    override fun times(a: Double, b: Double): Double {
        return a * b
    }

    override fun div(a: Double, b: Double): Double {
        return a / b
    }

    override fun rem(a: Double, b: Double): Double {
        return a % b
    }

    override fun sqrt(a: Double): Float {
        return kotlin.math.sqrt(a).toFloat()
    }

    override fun min(a: Double, b: Double): Double {
        return kotlin.math.min(a, b)
    }

    override fun max(a: Double, b: Double): Double {
        return kotlin.math.max(a, b)
    }

    override fun lessThan(a: Double, b: Double): Boolean {
        return a < b
    }

    override fun lequalThan(a: Double, b: Double): Boolean {
        return a <= b
    }

    override fun greaterThan(a: Double, b: Double): Boolean {
        return a > b
    }

    override fun gequalThan(a: Double, b: Double): Boolean {
        return a >= b
    }

    override fun abs(a: Double): Double {
        return kotlin.math.abs(a)
    }

    override fun negate(a: Double): Double {
        return -a
    }
}
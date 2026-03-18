package net.typho.big_shot_lib.api.math.op

interface OperatorSet<N : Number> {
    val zero: N
    val one: N

    fun convert(x: Number): N

    fun plus(a: N, b: N): N

    fun minus(a: N, b: N): N

    fun times(a: N, b: N): N

    fun div(a: N, b: N): N

    fun rem(a: N, b: N): N

    fun sqrt(a: N): Float

    fun min(a: N, b: N): N

    fun max(a: N, b: N): N

    fun lessThan(a: N, b: N): Boolean

    fun lequalThan(a: N, b: N): Boolean

    fun greaterThan(a: N, b: N): Boolean

    fun gequalThan(a: N, b: N): Boolean

    fun abs(a: N): N

    fun negate(a: N): N
}
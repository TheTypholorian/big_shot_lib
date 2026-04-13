package net.typho.big_shot_lib.api.math.vec

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3i

val IVec3<Int>.blockPos: BlockPos
    get() = BlockPos(x, y, z)

interface IVec3<N : Number> {
    val opSet: OperatorSet<N>

    val x: N
    val y: N
    val z: N

    val gridLength: N
        get() = opSet.plus(opSet.abs(x), opSet.plus(opSet.abs(y), opSet.abs(z)))
    val lengthSquared: N
        get() = opSet.plus(opSet.times(x, x), opSet.plus(opSet.times(y, y), opSet.times(z, z)))
    val length: Float
        get() = opSet.sqrt(lengthSquared)
    val abs: IVec3<N>
        get() = copyWith(opSet.abs(x), opSet.abs(y), opSet.abs(z))
    val xy: IVec2<N>
    val yz: IVec2<N>
    val xz: IVec2<N>

    val r: N
        get() = x
    val g: N
        get() = y
    val b: N
        get() = z

    val rg: IVec2<N>
        get() = xy
    val gb: IVec2<N>
        get() = yz
    val rb: IVec2<N>
        get() = xz

    fun copyWith(x: N, y: N, z: N): IVec3<N>

    fun toInt(): IVec3<Int> = NeoVec3i(x.toInt(), y.toInt(), z.toInt())

    fun toFloat(): IVec3<Float> = NeoVec3f(x.toFloat(), y.toFloat(), z.toFloat())

    fun toDouble(): IVec3<Double> = NeoVec3d(x.toDouble(), y.toDouble(), z.toDouble())

    fun lerp(x: N, y: N, z: N, d: Float): IVec3<N> {
        return copyWith(opSet.lerp(this.x, x, d), opSet.lerp(this.y, y, d), opSet.lerp(this.z, z, d))
    }

    fun plus(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.plus(this.x, x), opSet.plus(this.y, y), opSet.plus(this.z, z))
    }

    fun minus(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.minus(this.x, x), opSet.minus(this.y, y), opSet.minus(this.z, z))
    }

    fun times(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.times(this.x, x), opSet.times(this.y, y), opSet.times(this.z, z))
    }

    fun div(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.div(this.x, x), opSet.div(this.y, y), opSet.div(this.z, z))
    }

    fun rem(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.rem(this.x, x), opSet.rem(this.y, y), opSet.rem(this.z, z))
    }

    fun cross(x: N, y: N, z: N): IVec3<N> {
        return copyWith(
            opSet.plus(opSet.times(this.y, z), opSet.times(opSet.negate(this.z), y)),
            opSet.plus(opSet.times(this.z, x), opSet.times(opSet.negate(this.x), z)),
            opSet.plus(opSet.times(this.x, y), opSet.times(opSet.negate(this.y), x))
        )
    }

    fun min(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.min(this.x, x), opSet.min(this.y, y), opSet.min(this.z, z))
    }

    fun max(x: N, y: N, z: N): IVec3<N> {
        return copyWith(opSet.max(this.x, x), opSet.max(this.y, y), opSet.max(this.z, z))
    }

    fun distance(x: N, y: N, z: N): Float {
        return minus(x, y, z).length
    }

    fun distanceSquared(x: N, y: N, z: N): N {
        return minus(x, y, z).lengthSquared
    }

    fun gridDistance(x: N, y: N, z: N): N {
        return minus(x, y, z).gridLength
    }

    fun inDistance(x: N, y: N, z: N, dist: N): Boolean {
        return inDistanceSquared(x, y, z, opSet.times(dist, dist))
    }

    fun inDistanceSquared(x: N, y: N, z: N, dist: N): Boolean {
        return opSet.lessThan(distanceSquared(x, y, z), dist)
    }

    fun inGridDistance(x: N, y: N, z: N, dist: N): Boolean {
        return opSet.lessThan(gridDistance(x, y, z), dist)
    }

    fun minComponent(): N {
        return opSet.min(x, opSet.min(y, z))
    }

    fun maxComponent(): N {
        return opSet.max(x, opSet.max(y, z))
    }

    fun anyGreaterThan(x: N, y: N, z: N): Boolean {
        return opSet.greaterThan(this.x, x) || opSet.greaterThan(this.y, y) || opSet.greaterThan(this.z, z)
    }

    fun allGreaterThan(x: N, y: N, z: N): Boolean {
        return opSet.greaterThan(this.x, x) && opSet.greaterThan(this.y, y) && opSet.greaterThan(this.z, z)
    }

    fun anyGequalThan(x: N, y: N, z: N): Boolean {
        return opSet.gequalThan(this.x, x) || opSet.gequalThan(this.y, y) || opSet.gequalThan(this.z, z)
    }

    fun allGequalThan(x: N, y: N, z: N): Boolean {
        return opSet.gequalThan(this.x, x) && opSet.gequalThan(this.y, y) && opSet.gequalThan(this.z, z)
    }

    fun anyLessThan(x: N, y: N, z: N): Boolean {
        return opSet.lessThan(this.x, x) || opSet.lessThan(this.y, y) || opSet.lessThan(this.z, z)
    }

    fun allLessThan(x: N, y: N, z: N): Boolean {
        return opSet.lessThan(this.x, x) && opSet.lessThan(this.y, y) && opSet.lessThan(this.z, z)
    }

    fun anyLequalThan(x: N, y: N, z: N): Boolean {
        return opSet.lequalThan(this.x, x) || opSet.lequalThan(this.y, y) || opSet.lequalThan(this.z, z)
    }

    fun allLequalThan(x: N, y: N, z: N): Boolean {
        return opSet.lequalThan(this.x, x) && opSet.lequalThan(this.y, y) && opSet.lequalThan(this.z, z)
    }

    fun lerp(other: IVec3<N>, d: Float) = lerp(other.x, other.y, other.z, d)

    fun lerp(x: N, d: Float) = lerp(x, x, x, d)

    operator fun plus(other: IVec3<N>) = plus(other.x, other.y, other.z)

    operator fun plus(x: N) = plus(x, x, x)

    operator fun minus(other: IVec3<N>) = minus(other.x, other.y, other.z)

    operator fun minus(x: N) = minus(x, x, x)

    operator fun times(other: IVec3<N>) = times(other.x, other.y, other.z)

    operator fun times(x: N) = times(x, x, x)

    operator fun div(other: IVec3<N>) = div(other.x, other.y, other.z)

    operator fun div(x: N) = div(x, x, x)

    operator fun rem(other: IVec3<N>) = rem(other.x, other.y, other.z)

    operator fun rem(x: N) = rem(x, x, x)

    infix fun cross(other: IVec3<N>) = cross(other.x, other.y, other.z)

    infix fun cross(x: N) = cross(x, x, x)

    fun min(other: IVec3<N>) = min(other.x, other.y, other.z)

    fun min(x: N) = min(x, x, x)

    fun max(other: IVec3<N>) = max(other.x, other.y, other.z)

    fun max(x: N) = max(x, x, x)

    fun distance(other: IVec3<N>) = distance(other.x, other.y, other.z)

    fun distanceSquared(other: IVec3<N>) = distanceSquared(other.x, other.y, other.z)

    fun gridDistance(other: IVec3<N>) = gridDistance(other.x, other.y, other.z)

    fun inDistance(other: IVec3<N>, dist: N) = inDistance(other.x, other.y, other.z, dist)

    fun inDistanceSquared(other: IVec3<N>, dist: N) = inDistanceSquared(other.x, other.y, other.z, dist)

    fun inGridDistance(other: IVec3<N>, dist: N) = inGridDistance(other.x, other.y, other.z, dist)

    fun anyGreaterThan(other: IVec3<N>) = anyGreaterThan(other.x, other.y, other.z)

    fun anyGreaterThan(x: N) = anyGreaterThan(x, x, x)

    fun allGreaterThan(other: IVec3<N>) = allGreaterThan(other.x, other.y, other.z)

    fun allGreaterThan(x: N) = allGreaterThan(x, x, x)

    fun anyGequalThan(other: IVec3<N>) = anyGequalThan(other.x, other.y, other.z)

    fun anyGequalThan(x: N) = anyGequalThan(x, x, x)

    fun allGequalThan(other: IVec3<N>) = allGequalThan(other.x, other.y, other.z)

    fun allGequalThan(x: N) = allGequalThan(x, x, x)

    fun anyLessThan(other: IVec3<N>) = anyLessThan(other.x, other.y, other.z)

    fun anyLessThan(x: N) = anyLessThan(x, x, x)

    fun allLessThan(other: IVec3<N>) = allLessThan(other.x, other.y, other.z)

    fun allLessThan(x: N) = allLessThan(x, x, x)

    fun anyLequalThan(other: IVec3<N>) = anyLequalThan(other.x, other.y, other.z)

    fun anyLequalThan(x: N) = anyLequalThan(x, x, x)

    fun allLequalThan(other: IVec3<N>) = allLequalThan(other.x, other.y, other.z)

    fun allLequalThan(x: N) = allLequalThan(x, x, x)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = copyWith(opSet.negate(x), opSet.negate(y), opSet.negate(z))

    operator fun inc() = plus(opSet.one, opSet.one, opSet.one)

    operator fun dec() = minus(opSet.one, opSet.one, opSet.one)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException(index)
    }

    fun equals(x: N, y: N, z: N): Boolean {
        return this.x == x && this.y == y && this.z == z
    }

    fun equals(other: IVec3<N>): Boolean {
        return equals(other.x, other.y, other.z)
    }

    companion object {
        @JvmField
        val INT_CODEC: Codec<IVec3<Int>> = NeoCodecs.createList(3, Codec.INT, { NeoVec3i(it[0], it[1], it[2]) }, { listOf(it.x, it.y, it.z) })
        @JvmField
        val FLOAT_CODEC: Codec<IVec3<Float>> = NeoCodecs.createList(3, Codec.FLOAT, { NeoVec3f(it[0], it[1], it[2]) }, { listOf(it.x, it.y, it.z) })
        @JvmField
        val DOUBLE_CODEC: Codec<IVec3<Double>> = NeoCodecs.createList(3, Codec.DOUBLE, { NeoVec3d(it[0], it[1], it[2]) }, { listOf(it.x, it.y, it.z) })

        @JvmStatic
        fun IVec3<Int>.toJOML() = Vector3i(x, y, z)

        @JvmStatic
        fun IVec3<Float>.toJOML() = Vector3f(x, y, z)

        @JvmStatic
        fun IVec3<Double>.toJOML() = Vector3d(x, y, z)
    }
}

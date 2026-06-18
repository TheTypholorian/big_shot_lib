package net.typho.big_shot_lib.api.ext

import net.minecraft.world.phys.Vec3
import net.typho.big_shot_lib.api.math.op.DoubleOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2
import net.typho.big_shot_lib.api.math.vec.IVec3

interface Vec3Extension : IVec3<Double> {
    override val opSet: OperatorSet<Double>
        get() = DoubleOperatorSet
    override val xy: IVec2<Double>
        get() = IVec2(x, y)
    override val yz: IVec2<Double>
        get() = IVec2(y, z)
    override val xz: IVec2<Double>
        get() = IVec2(x, z)

    override fun copyWith(
        x: Double,
        y: Double,
        z: Double
    ) = Vec3(x, y, z)

    override fun lerp(
        x: Double,
        y: Double,
        z: Double,
        d: Float
    ): Vec3 {
        return super.lerp(x, y, z, d) as Vec3
    }

    override fun lerp(
        other: IVec3<Double>,
        d: Float
    ): Vec3 {
        return super.lerp(other, d) as Vec3
    }

    override fun lerp(x: Double, d: Float): Vec3 {
        return super.lerp(x, d) as Vec3
    }

    override fun plus(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.plus(x, y, z) as Vec3
    }

    override fun plus(other: IVec3<Double>): Vec3 {
        return super.plus(other) as Vec3
    }

    override fun plus(x: Double): Vec3 {
        return super.plus(x) as Vec3
    }

    override fun minus(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.minus(x, y, z) as Vec3
    }

    override fun minus(other: IVec3<Double>): Vec3 {
        return super.minus(other) as Vec3
    }

    override fun minus(x: Double): Vec3 {
        return super.minus(x) as Vec3
    }

    override fun times(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.times(x, y, z) as Vec3
    }

    override fun times(other: IVec3<Double>): Vec3 {
        return super.times(other) as Vec3
    }

    override fun times(x: Double): Vec3 {
        return super.times(x) as Vec3
    }

    override fun div(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.div(x, y, z) as Vec3
    }

    override fun div(other: IVec3<Double>): Vec3 {
        return super.div(other) as Vec3
    }

    override fun div(x: Double): Vec3 {
        return super.div(x) as Vec3
    }

    override fun rem(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.rem(x, y, z) as Vec3
    }

    override fun rem(other: IVec3<Double>): Vec3 {
        return super.rem(other) as Vec3
    }

    override fun rem(x: Double): Vec3 {
        return super.rem(x) as Vec3
    }

    override fun cross(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.cross(x, y, z) as Vec3
    }

    override fun cross(other: IVec3<Double>): Vec3 {
        return super.cross(other) as Vec3
    }

    override fun cross(x: Double): Vec3 {
        return super.cross(x) as Vec3
    }

    override fun min(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.min(x, y, z) as Vec3
    }

    override fun min(other: IVec3<Double>): Vec3 {
        return super.min(other) as Vec3
    }

    override fun min(x: Double): Vec3 {
        return super.min(x) as Vec3
    }

    override fun max(
        x: Double,
        y: Double,
        z: Double
    ): Vec3 {
        return super.max(x, y, z) as Vec3
    }

    override fun max(other: IVec3<Double>): Vec3 {
        return super.max(other) as Vec3
    }

    override fun max(x: Double): Vec3 {
        return super.max(x) as Vec3
    }

    override fun unaryPlus(): Vec3 {
        return super.unaryPlus() as Vec3
    }

    override fun unaryMinus(): Vec3 {
        return super.unaryMinus() as Vec3
    }

    override fun inc(): Vec3 {
        return super.inc() as Vec3
    }

    override fun dec(): Vec3 {
        return super.dec() as Vec3
    }
}
package net.typho.big_shot_lib.api.ext

import net.minecraft.core.Vec3i
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.math.IntOperatorSet
import net.typho.big_shot_lib.api.math.OperatorSet
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.plugin.Prefix
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.Extension.Companion.cast

@Prefix(BigShotLib.MOD_ID)
interface Vec3iExtension : Extension<Vec3i>, IVec3<Int> {
    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
    override val x: Int
        get() = cast().x
    override val y: Int
        get() = cast().y
    override val z: Int
        get() = cast().z
    override val xy: IVec2<Int>
        get() = IVec2(x, y)
    override val yz: IVec2<Int>
        get() = IVec2(y, z)
    override val xz: IVec2<Int>
        get() = IVec2(x, z)

    override fun copyWith(
        x: Int,
        y: Int,
        z: Int
    ) = Vec3i(x, y, z)

    override fun lerp(
        x: Int,
        y: Int,
        z: Int,
        d: Float
    ): Vec3i {
        return super.lerp(x, y, z, d) as Vec3i
    }

    override fun lerp(
        other: IVec3<Int>,
        d: Float
    ): Vec3i {
        return super.lerp(other, d) as Vec3i
    }

    override fun lerp(x: Int, d: Float): Vec3i {
        return super.lerp(x, d) as Vec3i
    }

    override fun plus(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.plus(x, y, z) as Vec3i
    }

    override fun plus(other: IVec3<Int>): Vec3i {
        return super.plus(other) as Vec3i
    }

    override fun plus(x: Int): Vec3i {
        return super.plus(x) as Vec3i
    }

    override fun minus(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.minus(x, y, z) as Vec3i
    }

    override fun minus(other: IVec3<Int>): Vec3i {
        return super.minus(other) as Vec3i
    }

    override fun minus(x: Int): Vec3i {
        return super.minus(x) as Vec3i
    }

    override fun times(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.times(x, y, z) as Vec3i
    }

    override fun times(other: IVec3<Int>): Vec3i {
        return super.times(other) as Vec3i
    }

    override fun times(x: Int): Vec3i {
        return super.times(x) as Vec3i
    }

    override fun div(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.div(x, y, z) as Vec3i
    }

    override fun div(other: IVec3<Int>): Vec3i {
        return super.div(other) as Vec3i
    }

    override fun div(x: Int): Vec3i {
        return super.div(x) as Vec3i
    }

    override fun rem(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.rem(x, y, z) as Vec3i
    }

    override fun rem(other: IVec3<Int>): Vec3i {
        return super.rem(other) as Vec3i
    }

    override fun rem(x: Int): Vec3i {
        return super.rem(x) as Vec3i
    }

    override fun cross(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.cross(x, y, z) as Vec3i
    }

    override fun cross(other: IVec3<Int>): Vec3i {
        return super.cross(other) as Vec3i
    }

    override fun cross(x: Int): Vec3i {
        return super.cross(x) as Vec3i
    }

    override fun min(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.min(x, y, z) as Vec3i
    }

    override fun min(other: IVec3<Int>): Vec3i {
        return super.min(other) as Vec3i
    }

    override fun min(x: Int): Vec3i {
        return super.min(x) as Vec3i
    }

    override fun max(
        x: Int,
        y: Int,
        z: Int
    ): Vec3i {
        return super.max(x, y, z) as Vec3i
    }

    override fun max(other: IVec3<Int>): Vec3i {
        return super.max(other) as Vec3i
    }

    override fun max(x: Int): Vec3i {
        return super.max(x) as Vec3i
    }

    override fun unaryPlus(): Vec3i {
        return super.unaryPlus() as Vec3i
    }

    override fun unaryMinus(): Vec3i {
        return super.unaryMinus() as Vec3i
    }

    override fun inc(): Vec3i {
        return super.inc() as Vec3i
    }

    override fun dec(): Vec3i {
        return super.dec() as Vec3i
    }

    override fun toVec3i(): Vec3i {
        return cast()
    }
}
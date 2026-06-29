package net.typho.big_shot_lib.api.ext

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.Extension.Companion.cast
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo

interface BlockPosExtension : Vec3iExtension {
    override fun copyWith(
        x: Int,
        y: Int,
        z: Int
    ) = BlockPos(x, y, z)

    override fun lerp(
        x: Int,
        y: Int,
        z: Int,
        d: Float
    ): BlockPos {
        return super.lerp(x, y, z, d) as BlockPos
    }

    override fun lerp(
        other: IVec3<Int>,
        d: Float
    ): BlockPos {
        return super.lerp(other, d) as BlockPos
    }

    override fun lerp(x: Int, d: Float): BlockPos {
        return super.lerp(x, d) as BlockPos
    }

    override fun plus(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.plus(x, y, z) as BlockPos
    }

    override fun plus(other: IVec3<Int>): BlockPos {
        return super.plus(other) as BlockPos
    }

    override fun plus(x: Int): BlockPos {
        return super.plus(x) as BlockPos
    }

    override fun minus(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.minus(x, y, z) as BlockPos
    }

    override fun minus(other: IVec3<Int>): BlockPos {
        return super.minus(other) as BlockPos
    }

    override fun minus(x: Int): BlockPos {
        return super.minus(x) as BlockPos
    }

    override fun times(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.times(x, y, z) as BlockPos
    }

    override fun times(other: IVec3<Int>): BlockPos {
        return super.times(other) as BlockPos
    }

    override fun times(x: Int): BlockPos {
        return super.times(x) as BlockPos
    }

    override fun div(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.div(x, y, z) as BlockPos
    }

    override fun div(other: IVec3<Int>): BlockPos {
        return super.div(other) as BlockPos
    }

    override fun div(x: Int): BlockPos {
        return super.div(x) as BlockPos
    }

    override fun rem(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.rem(x, y, z) as BlockPos
    }

    override fun rem(other: IVec3<Int>): BlockPos {
        return super.rem(other) as BlockPos
    }

    override fun rem(x: Int): BlockPos {
        return super.rem(x) as BlockPos
    }

    override fun cross(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.cross(x, y, z) as BlockPos
    }

    override fun cross(other: IVec3<Int>): BlockPos {
        return super.cross(other) as BlockPos
    }

    override fun cross(x: Int): BlockPos {
        return super.cross(x) as BlockPos
    }

    override fun min(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.min(x, y, z) as BlockPos
    }

    override fun min(other: IVec3<Int>): BlockPos {
        return super.min(other) as BlockPos
    }

    override fun min(x: Int): BlockPos {
        return super.min(x) as BlockPos
    }

    override fun max(
        x: Int,
        y: Int,
        z: Int
    ): BlockPos {
        return super.max(x, y, z) as BlockPos
    }

    override fun max(other: IVec3<Int>): BlockPos {
        return super.max(other) as BlockPos
    }

    override fun max(x: Int): BlockPos {
        return super.max(x) as BlockPos
    }

    override fun unaryPlus(): BlockPos {
        return super.unaryPlus() as BlockPos
    }

    override fun unaryMinus(): BlockPos {
        return super.unaryMinus() as BlockPos
    }

    override fun inc(): BlockPos {
        return super.inc() as BlockPos
    }

    override fun dec(): BlockPos {
        return super.dec() as BlockPos
    }

    override fun immutable(): IVec3<Int> {
        return toBlockPos()
    }

    override fun toBlockPos(): BlockPos {
        return castTo<Vec3i, BlockPos>().immutable()
    }
}
package net.typho.big_shot_lib.api.math

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.math.NeoDirection.DOWN
import net.typho.big_shot_lib.api.math.NeoDirection.EAST
import net.typho.big_shot_lib.api.math.NeoDirection.NORTH
import net.typho.big_shot_lib.api.math.NeoDirection.SOUTH
import net.typho.big_shot_lib.api.math.NeoDirection.UP
import net.typho.big_shot_lib.api.math.NeoDirection.WEST
import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec2i
import net.typho.big_shot_lib.api.math.vec.NeoVec3i

val Direction.neo: NeoDirection
    get() = when (this) {
        Direction.DOWN -> DOWN
        Direction.UP -> UP
        Direction.NORTH -> NORTH
        Direction.SOUTH -> SOUTH
        Direction.WEST -> WEST
        Direction.EAST -> EAST
    }

enum class NeoDirection(
    @JvmField
    val mojang: Direction,
    @JvmField
    val axisDirection: AxisDirection,
    @JvmField
    val axis: Axis,
    override val x: Int,
    override val y: Int,
    override val z: Int
) : IVec3<Int> {
    EAST(Direction.EAST, AxisDirection.POSITIVE, Axis.X, 1, 0, 0),
    WEST(Direction.WEST, AxisDirection.NEGATIVE, Axis.X, -1, 0, 0),
    UP(Direction.UP, AxisDirection.POSITIVE, Axis.Y, 0, 1, 0),
    DOWN(Direction.DOWN, AxisDirection.NEGATIVE, Axis.Y, 0, -1, 0),
    SOUTH(Direction.SOUTH, AxisDirection.POSITIVE, Axis.Z, 0, 0, 1),
    NORTH(Direction.NORTH, AxisDirection.NEGATIVE, Axis.Z, 0, 0, -1);

    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
    override val xy: IVec2<Int>
        get() = NeoVec2i(x, y)
    override val yz: IVec2<Int>
        get() = NeoVec2i(y, z)
    override val xz: IVec2<Int>
        get() = NeoVec2i(x, z)

    override fun copyWith(
        x: Int,
        y: Int,
        z: Int
    ): IVec3<Int> = NeoVec3i(x, y, z)

    enum class AxisDirection(
        @JvmField
        val inc: Int
    ) {
        POSITIVE(1),
        NEGATIVE(-1)
    }

    enum class Axis {
        X,
        Y,
        Z
    }
}
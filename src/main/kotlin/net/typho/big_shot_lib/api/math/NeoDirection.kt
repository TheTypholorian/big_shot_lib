package net.typho.big_shot_lib.api.math

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3i

enum class NeoDirection(
    @JvmField
    val mojang: Direction,
    @JvmField
    val axisDirection: AxisDirection,
    @JvmField
    val axis: Axis,
    @JvmField
    val inc: AbstractVec3<Int, *>
) {
    EAST(Direction.EAST, AxisDirection.POSITIVE, Axis.X, NeoVec3i(1, 0, 0)),
    WEST(Direction.WEST, AxisDirection.NEGATIVE, Axis.X, NeoVec3i(-1, 0, 0)),
    UP(Direction.UP, AxisDirection.POSITIVE, Axis.Y, NeoVec3i(0, 1, 0)),
    DOWN(Direction.DOWN, AxisDirection.NEGATIVE, Axis.Y, NeoVec3i(0, -1, 0)),
    SOUTH(Direction.SOUTH, AxisDirection.POSITIVE, Axis.Z, NeoVec3i(0, 0, 1)),
    NORTH(Direction.NORTH, AxisDirection.NEGATIVE, Axis.Z, NeoVec3i(0, 0, -1));

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
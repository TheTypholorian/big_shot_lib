package net.typho.big_shot_lib.api.ext

import net.typho.big_shot_lib.api.math.op.IntOperatorSet
import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.IVec2
import net.typho.big_shot_lib.api.math.vec.IVec3

interface DirectionExtension : IVec3<Int> {
    override val opSet: OperatorSet<Int>
        get() = IntOperatorSet
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
    ): IVec3<Int> {
        return IVec3(x, y, z)
    }
}
package net.typho.big_shot_lib.api.ext

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.math.IntOperatorSet
import net.typho.big_shot_lib.api.math.OperatorSet
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.common.annotation.Prefix

@Prefix(BigShotLib.MOD_ID)
interface DirectionExtension : Extension<Direction>, IVec3<Int> {
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
package net.typho.big_shot_lib.api.ext

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.common.annotation.Prefix

@Prefix(BigShotLib.MOD_ID)
interface MutableBlockPosExtension : BlockPosExtension {
    override var x: Int
        get() = castTo<Vec3i, BlockPos.MutableBlockPos>().x
        set(value) {
            castTo<Vec3i, BlockPos.MutableBlockPos>().setX(value)
        }
    override var y: Int
        get() = castTo<Vec3i, BlockPos.MutableBlockPos>().y
        set(value) {
            castTo<Vec3i, BlockPos.MutableBlockPos>().setY(value)
        }
    override var z: Int
        get() = castTo<Vec3i, BlockPos.MutableBlockPos>().z
        set(value) {
            castTo<Vec3i, BlockPos.MutableBlockPos>().setZ(value)
        }
    override var xy: IVec2<Int>
    override var yz: IVec2<Int>
    override var xz: IVec2<Int>
    override var r: Int
        get() = super.r
        set(value) {
            x = value
        }
    override var g: Int
        get() = super.g
        set(value) {
            y = value
        }
    override var b: Int
        get() = super.b
        set(value) {
            z = value
        }
    override var rg: IVec2<Int>
        get() = super.rg
        set(value) {
            xy = value
        }
    override var gb: IVec2<Int>
        get() = super.gb
        set(value) {
            yz = value
        }
    override var rb: IVec2<Int>
        get() = super.rb
        set(value) {
            xz = value
        }
}
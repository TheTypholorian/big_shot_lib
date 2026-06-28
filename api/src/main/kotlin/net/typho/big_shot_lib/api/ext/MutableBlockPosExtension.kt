package net.typho.big_shot_lib.api.ext

import net.typho.big_shot_lib.api.math.IVec2

interface MutableBlockPosExtension : BlockPosExtension {
    override var x: Int
    override var y: Int
    override var z: Int
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
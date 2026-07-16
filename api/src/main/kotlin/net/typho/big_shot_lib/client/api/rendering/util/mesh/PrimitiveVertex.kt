package net.typho.big_shot_lib.client.api.rendering.util.mesh

import net.typho.big_shot_lib.client.api.rendering.util.PackedLight

open class PrimitiveVertex(
    @JvmField
    var x: Float,
    @JvmField
    var y: Float,
    @JvmField
    var z: Float,
    @JvmField
    var color: Int,
    @JvmField
    var u: Float,
    @JvmField
    var v: Float,
    @JvmField
    var light: Int,
    @JvmField
    var normal: Int
) {
    constructor() : this(0f, 0f, 0f, 0, 0f, 0f, 0, 0)

    constructor(other: PrimitiveVertex, offsetX: Float, offsetY: Float, offsetZ: Float) : this(
        other.x + offsetX,
        other.y + offsetY,
        other.z + offsetZ,
        other.color,
        other.u,
        other.v,
        other.light,
        other.normal
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrimitiveVertex) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (color != other.color) return false
        if (u != other.u) return false
        if (v != other.v) return false
        if (light != other.light) return false
        if (normal != other.normal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + color
        result = 31 * result + u.hashCode()
        result = 31 * result + v.hashCode()
        result = 31 * result + light
        result = 31 * result + normal
        return result
    }

    override fun toString(): String {
        return "Vertex(pos=($x, $y, $z), color=0x${color.toHexString()}, uv=($u, $v), light=(sky=${PackedLight.unpackSky(light)}, block=${PackedLight.unpackBlock(light)}), normal=$normal)"
    }
}
package net.typho.big_shot_lib.api.client.util.panorama

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.math.NeoDirection

@JvmRecord
data class PanoramaTexture(
    @JvmField
    val south: Identifier,
    @JvmField
    val east: Identifier,
    @JvmField
    val north: Identifier,
    @JvmField
    val west: Identifier,
    @JvmField
    val up: Identifier,
    @JvmField
    val down: Identifier,
) {
    constructor(location: Identifier) : this(
        location.withPath { it + "_0.png" },
        location.withPath { it + "_1.png" },
        location.withPath { it + "_2.png" },
        location.withPath { it + "_3.png" },
        location.withPath { it + "_4.png" },
        location.withPath { it + "_5.png" }
    )

    fun get(dir: NeoDirection): Identifier {
        return when (dir) {
            NeoDirection.DOWN -> down
            NeoDirection.UP -> up
            NeoDirection.NORTH -> north
            NeoDirection.SOUTH -> south
            NeoDirection.WEST -> west
            NeoDirection.EAST -> east
        }
    }
}

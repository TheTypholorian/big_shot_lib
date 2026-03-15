package net.typho.big_shot_lib.api.client.util.panorama

import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

@JvmRecord
data class PanoramaTexture(
    @JvmField
    val south: NeoIdentifier,
    @JvmField
    val east: NeoIdentifier,
    @JvmField
    val north: NeoIdentifier,
    @JvmField
    val west: NeoIdentifier,
    @JvmField
    val up: NeoIdentifier,
    @JvmField
    val down: NeoIdentifier,
) {
    constructor(location: NeoIdentifier) : this(
        location.withPath { it + "_0.png" },
        location.withPath { it + "_1.png" },
        location.withPath { it + "_2.png" },
        location.withPath { it + "_3.png" },
        location.withPath { it + "_4.png" },
        location.withPath { it + "_5.png" }
    )

    fun get(dir: NeoDirection): NeoIdentifier {
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

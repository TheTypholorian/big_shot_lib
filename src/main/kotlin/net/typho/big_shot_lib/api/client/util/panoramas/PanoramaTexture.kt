package net.typho.big_shot_lib.api.client.util.panoramas

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

@JvmRecord
data class PanoramaTexture(
    @JvmField
    val south: ResourceIdentifier,
    @JvmField
    val east: ResourceIdentifier,
    @JvmField
    val north: ResourceIdentifier,
    @JvmField
    val west: ResourceIdentifier,
    @JvmField
    val up: ResourceIdentifier,
    @JvmField
    val down: ResourceIdentifier,
) {
    constructor(location: ResourceIdentifier) : this(
        location.withPath { it + "_0.png" },
        location.withPath { it + "_1.png" },
        location.withPath { it + "_2.png" },
        location.withPath { it + "_3.png" },
        location.withPath { it + "_4.png" },
        location.withPath { it + "_5.png" }
    )

    fun get(dir: Direction): ResourceIdentifier {
        return when (dir) {
            Direction.DOWN -> down
            Direction.UP -> up
            Direction.NORTH -> north
            Direction.SOUTH -> south
            Direction.WEST -> west
            Direction.EAST -> east
        }
    }
}

package net.typho.big_shot_lib.api.client.util.panoramas

import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.NeoIdentifier

@JvmRecord
data class PanoramaSet(
    override val location: NeoIdentifier,
    @JvmField
    val priority: PanoramaPriority,
    @JvmField
    val textures: List<PanoramaTexture>,
    @JvmField
    val interval: Long? = 5000,
    @JvmField
    val fade: Long? = 1000
) : NamedResource {
    constructor(
        location: NeoIdentifier,
        priority: PanoramaPriority,
        texture: PanoramaTexture
    ) : this(location, priority, listOf(texture), null)

    val doesCycle: Boolean
        get() = interval != null && interval > 0

    companion object {
        @JvmStatic
        fun select(vararg sets: PanoramaSet): PanoramaSet? {
            return sets.maxByOrNull { it.priority.ordinal }
        }
    }
}
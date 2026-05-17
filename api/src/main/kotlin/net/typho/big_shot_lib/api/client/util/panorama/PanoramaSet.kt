package net.typho.big_shot_lib.api.client.util.panorama

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.resource.NamedResource

@JvmRecord
data class PanoramaSet(
    override val location: Identifier,
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
        location: Identifier,
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
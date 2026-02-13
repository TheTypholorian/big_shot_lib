package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.Named
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class RenderSettings(
    @JvmField
    val location: ResourceIdentifier,
    @JvmField
    val shards: List<RenderSettingShard>
) : Named, GlBindable {
    override fun location() = location

    override fun bind() {
        shards.forEach(RenderSettingShard::bind)
    }

    override fun unbind() {
        shards.forEach(RenderSettingShard::unbind)
    }

    companion object {
        @JvmField
        val REGISTRY = HashMap<ResourceIdentifier, RenderSettings>()
    }
}
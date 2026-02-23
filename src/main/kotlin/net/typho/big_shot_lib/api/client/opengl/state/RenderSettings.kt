package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class RenderSettings(
    @JvmField
    val location: ResourceIdentifier,
    @JvmField
    val shards: List<RenderSettingShard>
) : NamedResource, GlBindable {
    override fun location() = location

    override fun bind(pushStack: Boolean) {
        shards.forEach(RenderSettingShard::bind)
    }

    override fun unbind(popStack: Boolean) {
        shards.forEach(RenderSettingShard::unbind)
    }

    companion object {
        @JvmField
        val REGISTRY = HashMap<ResourceIdentifier, RenderSettings>()
    }
}
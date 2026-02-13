package net.typho.big_shot_lib.api.state

import net.typho.big_shot_lib.api.util.Bindable
import net.typho.big_shot_lib.api.util.Named
import net.typho.big_shot_lib.api.util.ResourceIdentifier

open class RenderSettings(
    @JvmField
    val location: ResourceIdentifier,
    @JvmField
    val shards: List<RenderSettingShard>
) : Named, Bindable {
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
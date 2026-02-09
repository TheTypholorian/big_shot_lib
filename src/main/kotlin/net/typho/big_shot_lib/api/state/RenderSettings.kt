package net.typho.big_shot_lib.api.state

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.util.Bindable
import net.typho.big_shot_lib.api.util.Named

open class RenderSettings(
    @JvmField
    val location: ResourceLocation,
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
        val REGISTRY = HashMap<ResourceLocation, RenderSettings>()
    }
}
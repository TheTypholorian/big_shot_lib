package net.typho.big_shot_lib.api.render_queue

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.Named

open class RenderSettings(
    @JvmField
    val location: ResourceLocation,
    @JvmField
    val shards: List<RenderSettingShard>
) : Named, Bindable {
    override fun location() = location

    override fun bind() {
        TODO("Not yet implemented")
    }

    override fun unbind() {
        TODO("Not yet implemented")
    }
}
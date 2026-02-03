package net.typho.big_shot_lib.state

import net.minecraft.resources.ResourceLocation

class NeoRenderSettings(
    val location: ResourceLocation,
    val settings: RenderSettingsInfo
) : IRenderSettingArray {
    override fun bufferSize() = settings.bufferSize

    override fun format() = settings.format

    override fun mode() = settings.mode

    override fun sort() = settings.sort

    override fun location() = location

    override fun push() {
        for (shard in settings.shards) {
            shard.push()
        }
    }

    override fun pop() {
        for (shard in settings.shards) {
            shard.pop()
        }
    }
}
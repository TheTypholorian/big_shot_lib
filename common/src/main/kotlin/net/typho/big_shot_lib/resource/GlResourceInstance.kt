package net.typho.big_shot_lib.resource

import net.minecraft.resources.ResourceLocation

interface GlResourceInstance {
    fun release()

    fun location(): ResourceLocation?

    fun type(): GlResourceType

    fun id(): Int

    fun bind() = type().bind(id())

    fun unbind() = type().unbind()

    fun canHotswapBind() = true
}
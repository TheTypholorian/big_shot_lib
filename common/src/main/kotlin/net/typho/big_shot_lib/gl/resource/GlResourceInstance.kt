package net.typho.big_shot_lib.gl.resource

import net.minecraft.resources.ResourceLocation

interface GlResourceInstance {
    fun release()

    fun location(): ResourceLocation?

    fun type(): GlResourceType?

    fun id(): Int
}
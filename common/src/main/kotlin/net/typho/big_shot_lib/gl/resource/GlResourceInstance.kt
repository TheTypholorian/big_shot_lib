package net.typho.big_shot_lib.gl.resource

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.GlStack

interface GlResourceInstance {
    fun release()

    fun location(): ResourceLocation?

    fun type(): GlResourceType

    fun id(): Int

    fun bind() = type().bind(id())

    fun bind(stack: GlStack) {
        stack.bind(this)
        bind()
    }

    fun unbind() = type().unbind()
}
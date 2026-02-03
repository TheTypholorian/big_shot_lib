package net.typho.big_shot_lib.state.gl

import net.minecraft.resources.ResourceLocation

interface GlState<T> {
    fun location(): ResourceLocation

    fun default(): T

    fun queryValue(): T?

    fun set(value: T)

    fun includedFlags(): Collection<GlFlag> = listOf()
}
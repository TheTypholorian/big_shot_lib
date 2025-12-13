package net.typho.vibrancy.gl

import net.typho.big_shot_lib.GlResourceInstance
import net.typho.big_shot_lib.GlResourceStack
import net.typho.big_shot_lib.Unbindable

interface Bindable<T : Unbindable> : GlResourceInstance {
    fun bind(): T

    fun bind(stack: GlResourceStack): T
}
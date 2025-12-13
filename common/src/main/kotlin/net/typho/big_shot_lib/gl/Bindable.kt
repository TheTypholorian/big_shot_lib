package net.typho.big_shot_lib.gl

interface Bindable<T : Unbindable> : GlResourceInstance {
    fun bind(): T

    fun bind(stack: GlResourceStack): T
}